package com.example.demo.service;

import com.example.demo.entities.*;
import com.example.demo.repository.PatientRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class AiQuestionsService {

    private final ChatClient chatClient;
    private final AppointmentService appointmentService;
    private final PatientRecordService patientRecordService;
    private final PatientService patientService;
    private final PatientRepository patientRepository;

public List<AiSuggesionQuestions> generateAiQuestion(Integer patientId , Integer appointmentId){

    String data = null;
    try {
        data = this.getData(patientId,appointmentId);
    } catch (Exception e) {
        throw new RuntimeException(e);
    }
    if (data == null) {
        throw new RuntimeException("data is null");
    }
    String response = chatClient.prompt().user(
            """
               You are a professional doctor assistant.

               After reading the patient data, generate 3 to 7 questions
               that could reveal possible diseases that might be missed
               by the doctor.

               RULES:
                   - Respond ONLY with valid JSON array.
                   - Do NOT add trailing commas after the last item .
                   - Use standard JSON syntax.
               - Format:
               [
                 {"question":"question text"}, {"question":"question text"} ,....
               ]

               Patient Data:
               %s
               """.formatted(data))
            .call().content();
    System.out.println("*-*-*-*-**-*-*-*-*-*-*-*-*");
    System.out.println(response);
    System.out.println("*-*-*-*-**-*-*-*-*-*-*-*-*");
    // 2️⃣ Parse JSON response
    String cleanedResponse = response.trim()
            .replace("\\\"", "\"")                // fix escaped quotes
            .replaceAll(",\\s*]", "]")           // remove trailing commas before closing array
            .replaceAll("\\?}", "?")             // fix broken question ending
            .replaceAll("\\}\\s*]", "}]");
    ObjectMapper mapper = new ObjectMapper();
    List<AiSuggesionQuestions> result = new ArrayList<>();

    try {
        List<Map<String, String>> parsed =
                mapper.readValue(cleanedResponse, new TypeReference<List<Map<String, String>>>() {});

        Appointment appointment =
                appointmentService.getAppointmentsById(patientId, appointmentId);
        Patient patient = patientService.findById(patientId).get();
        for (Map<String, String> item : parsed) {
            AiSuggesionQuestions q = new AiSuggesionQuestions();
            q.setQuestion(item.get("question"));
            q.setAnswer("vide");
            q.setAppointment(appointment);

            patient.getPatientRecord().getAppointmentsHistory().stream()
                            .filter(a->a.getId().equals(appointmentId))
                                    .findFirst().ifPresent(a->a.getAiSuggesionQuestions().add(q));

            result.add(q);
        }
        patientRepository.save(patient);

    } catch (Exception e) {
        throw new RuntimeException("Invalid AI JSON response: " + response, e);
    }

    return result;
}




public String getData(Integer patientId , Integer appointmentId) throws Exception {
String data ;
    PatientRecord record = patientRecordService.getPatientRecord(patientId);
    Appointment appointment = appointmentService.getAppointmentsById(patientId, appointmentId);
    data = record.toString() + "\n" + appointment.toString();
    return data;
}

public void respondToQuestion(Integer patientId , Integer appointmentId ,Integer questionId, String data){
    Patient  patient = patientRepository.findById(patientId)
            .orElseThrow(() -> new RuntimeException("Patient not found"));

    Appointment appointment = patient.getPatientRecord().getAppointmentsHistory().stream()
            .filter(ap -> ap.getId().equals(appointmentId)).findFirst().orElseThrow(() -> new RuntimeException("Appointment not found"));

    appointment.getAiSuggesionQuestions().stream().filter(q -> q.getId().equals(questionId)).findFirst()
            .ifPresent(q->q.setAnswer(data));
    patientRepository.save(patient);

}

public String getAiSuggesionData(Integer patientId , Integer appointmentId){
    String data = " -> " ;
    Patient  patient = patientRepository.findById(patientId)
            .orElseThrow(() -> new RuntimeException("Patient not found"));

    Appointment app = patient.getPatientRecord().getAppointmentsHistory().stream()
            .filter(appointment -> appointment.getId().equals(appointmentId)).findFirst()
            .get();
    List<AiSuggesionQuestions> questions = app.getAiSuggesionQuestions();
   for (AiSuggesionQuestions q : questions) {
       if (q.getAnswer()!=null && q.getAnswer().equals("vide")) {
           data += q.toString();
           data += " / " ;
       }
   }
   return data;
}

public AiDetector getAiResult(Integer patientId , Integer appointmentId){
    String patientData = "";
    String AiSuggesionData = "";
    try {
        patientData = this.getData(patientId,appointmentId);
    } catch (Exception e) {
        throw new RuntimeException(e);
    }
    if (patientData == null) {
        throw new RuntimeException("data is null");
    }
    try {
        AiSuggesionData = this.getAiSuggesionData(patientId,appointmentId);
    } catch (Exception e) {
        throw new RuntimeException(e);
    }
    if (AiSuggesionData == null) {
        throw new RuntimeException("data is null");
    }
    String response = chatClient.prompt().user(
                    """
                            You are a professional medical assistant supporting a licensed doctor.
                            Your role is to analyze patient information and highlight potential medical conditions
                            that the doctor may need to investigate further.
                            
                            CONTEXT:
                            1. Previously, you generated medical questions based on:
                               - The patient’s appointment cause
                               - The patient’s health record
                               These questions were designed to help detect potential health problems
                               that could be overlooked.
                            
                            2. Now, you will receive:
                               - The appointment cause
                               - The patient’s health record
                               - The previously generated questions WITH the patient’s answers
                            
                            TASK:
                            Analyze the provided data and identify any potential medical conditions
                            that may exist based on symptoms, history, and answers.
                            
                            IMPORTANT MEDICAL DISCLAIMER:
                            - You are NOT providing a diagnosis.
                            - You are only identifying potential conditions for further medical evaluation.
                            
                            OUTPUT RULES (STRICT):
                            - Respond ONLY with a valid JSON array.
                            - Do NOT include explanations, markdown, or extra text.
                            - Do NOT add trailing commas.
                            - Use standard JSON syntax only.
                            - If the provided data is insufficient, irrelevant, or out of context,
                                   you MUST return "no result" for all fields.
                            
                            
                            WARNING LEVEL:
                            The value of "level_of_warning" MUST be one of the following:
                            - "LOW"
                            - "MODERATE"
                            - "HIGH"
                            -"no result"
                            
                            OUTPUT FORMAT (EXACT):
                            [
                              {
                                "potentiel_maladie": "string or 'no result' ",
                                "aiAnalyse": "string or 'no result'",
                                "level_of_warning": "LOW | MODERATE | HIGH | no result"
                              }
                            ]
                            
                            PATIENT DATA:
                            %s
                            Question and answer Data :
                            %s
                            
                       """.formatted(patientData,AiSuggesionData))
            .call().content();
    System.out.println("*-*-*-*-**-*-*-*-*-*-*-*-*");
    System.out.println(response);
    System.out.println("*-*-*-*-**-*-*-*-*-*-*-*-*");
    // 2️⃣ Parse JSON response
    String cleanedResponse = response
            .replaceAll("(?s)```json", "") // remove starting ```json
            .replaceAll("(?s)```", "")     // remove ending ```
            .trim()
            .replace("\\\"", "\"")
            .replaceAll(",\\s*]", "]")
            .replaceAll("\\}\\s*]", "}]");
    ObjectMapper mapper = new ObjectMapper();
    try {
        List<Map<String, String>> parsed =
                mapper.readValue(cleanedResponse,
                        new TypeReference<List<Map<String, String>>>() {});

        Appointment appointment =
                appointmentService.getAppointmentsById(patientId, appointmentId);

        Patient patient =
                patientService.findById(patientId)
                        .orElseThrow(() -> new RuntimeException("Patient not found"));

        // 3️⃣ One appointment → one detector
        Map<String, String> item = parsed.get(0);

        AiDetector detector = new AiDetector();
        detector.setWarning(item.getOrDefault("potentiel_maladie", "no result"));
        detector.setAiAnalyse(item.getOrDefault("aiAnalyse", "no result"));
        detector.setLevel(item.getOrDefault("level_of_warning", "no result"));
        detector.setAppointment(appointment);

        // 4️⃣ Attach to appointment
        appointment.setAiDetector(detector);

        // 5️⃣ Persist
        patientRepository.save(patient);

        return detector;

    } catch (Exception e) {
        throw new RuntimeException("Invalid AI JSON response: " + response, e);
    }
}
}
