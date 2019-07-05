package ua.nekl08.varus;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import ua.nekl08.varus.model.Document;
import ua.nekl08.varus.model.Template;
import ua.nekl08.varus.repository.DocumentRepository;
import ua.nekl08.varus.repository.TemplateRepository;

import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.util.Random;

@SpringBootApplication
public class VarusApplication {

    public static void main(String[] args) {
        SpringApplication.run(VarusApplication.class, args);
    }

//    Наполнить базу данных
    @Component
    class Populator{
        @Autowired
        private TemplateRepository templateRepository;
        @Autowired
        private DocumentRepository documentRepository;

        @PostConstruct
        public void init() {
            Document document = new Document();
            document.setName("Документ");
            document.setType("Шаблон документа");
            document.setDate(LocalDate.now());
            document.setDescription("Обычный такой себе шаблончик");
            document.setTermYear(3);
            document.setUrl("https://www.google.com.ua/");
            document.getFieldAndValue().put("поле №1", "значение 1");
            document.getFieldAndValue().put("поле №2", "значение 2");
            documentRepository.save(document);

            Template template = new Template();
            template.setName("Шаблон документа");
            template.setAuthor("Вася Пупкин");
            template.setDate(LocalDate.now());
            template.setEnableFieldFile(true);
            template.getFields().add("поле №1");
            template.getFields().add("поле №2");
            template.getFields().add("поле №3");
            templateRepository.save(template);
        }
    }
}

