package co.edu.uptc.edamicrokafka.service;

import co.edu.uptc.edamicrokafka.model.Login;
import co.edu.uptc.edamicrokafka.utils.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class LoginEventProducer {
    private static final String TOPIC_ADD = "addlogin_events";
    private static final String TOPIC_EDIT = "editlogin_events";
    private static final String TOPIC_FINDBYID = "findloginbyid_events";
    private static final String TOPIC_FINDALL = "findalllogins_events";

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplateAdd;
    
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplateEdit;
    
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplateFindById;
    
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplateFindAll;

    public void sendAddLoginEvent(Login login) {
        String json = new JsonUtils().toJson(login);
        kafkaTemplateAdd.send(TOPIC_ADD, json);
    }

    public void sendEditLoginEvent(Login login) {
        String json = new JsonUtils().toJson(login);
        kafkaTemplateEdit.send(TOPIC_EDIT, json);
    }

    public void sendFindByLoginIdEvent(String username) {
        kafkaTemplateFindById.send(TOPIC_FINDBYID, username);
    }

    public void sendFindAllLoginsEvent(String logins) {
        kafkaTemplateFindAll.send(TOPIC_FINDALL, logins);
    }
}
