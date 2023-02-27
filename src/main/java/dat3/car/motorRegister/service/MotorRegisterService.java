package dat3.car.motorRegister.service;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import dat3.car.motorRegister.dto.MotorRegisterResponse;
import dat3.car.motorRegister.entity.MotorRegister;
import dat3.car.motorRegister.repository.MotorRegisterRepository;

/* https://www.twilio.com/blog/5-ways-to-make-http-requests-in-java
    "Close your eyes and center yourself in 1997. 
     Titanic was rocking the box office and inspiring a thousand memes, 
     Spice Girls had a best-selling album, but the biggest news of the year 
     was surely HttpURLConnection being added to Java 1.1."
*/

@Service
public class MotorRegisterService {

    @Autowired
    private MotorRegisterRepository motorRegisterRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Value("${motor.api.url}")
    private String apiUrl;

    @Value("${motor.api.key}")
    private String apiKey;

    public List<MotorRegisterResponse> lookup(String registrationNumber)
    {
        // Check if we already looked up this registration number earlier to save API request.
        // The API is limited to 100 request.
        Optional<MotorRegister> motorRegisterOpt = motorRegisterRepository.findById(registrationNumber);
        if (motorRegisterOpt.isPresent())
            return Arrays.asList(new MotorRegisterResponse[] { new MotorRegisterResponse(motorRegisterOpt.get()) });
        else
        {
            List<MotorRegisterResponse> responses = lookupAPI(registrationNumber);            
            CacheMotorRegisterResponses(responses);            
            return responses;
        }
    }

    private List<MotorRegisterResponse> lookupAPI(String registrationNumber) {
        List<MotorRegisterResponse> responses = new LinkedList<MotorRegisterResponse>();

        try {
            String endpoint = String.format("/vehicles?registration_number=%s", registrationNumber);

            URL url = new URL(String.format("%s%s", apiUrl, endpoint));
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("X-AUTH-TOKEN", apiKey);
            InputStream responseStream = connection.getInputStream();
            
            TypeReference<List<MotorRegisterResponse>> typeRef = new TypeReference<List<MotorRegisterResponse>>() {};
            responses = objectMapper.readValue(responseStream, typeRef);
        
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return responses;
    }

    private void CacheMotorRegisterResponses(List<MotorRegisterResponse> responses)
    {
        if (responses.size() == 0)
            return;
            
        List<MotorRegister> motorRegisters = responses.stream().map(response -> response.toMotorRegister()).collect(Collectors.toList());
        motorRegisterRepository.saveAll(motorRegisters);
    }
}
