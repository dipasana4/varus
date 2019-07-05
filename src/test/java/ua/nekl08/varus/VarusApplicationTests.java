package ua.nekl08.varus;

import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ua.nekl08.varus.model.Document;
import ua.nekl08.varus.model.Template;
import ua.nekl08.varus.repository.DocumentRepository;
import ua.nekl08.varus.repository.TemplateRepository;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.fileUpload;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@SpringBootTest
public class VarusApplicationTests {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private DocumentRepository documentRepository;

    @MockBean
    private TemplateRepository templateRepository;

    private Document[] getDocuments() {
        Document doc1 = new Document();
        doc1.setName("doc1");
        doc1.setType("type1");
        doc1.setDate(LocalDate.now());
        Document doc2 = new Document();
        doc2.setName("doc2");
        doc2.setType("type2");
        doc2.setDate(LocalDate.now());
        return new Document[]{doc1, doc2};
    }

    private Template[] getTemplates() {
        Template template1 = new Template();
        template1.setName("doc1");
        template1.setAuthor("Author1");
        template1.setDate(LocalDate.now());
        Template template2 = new Template();
        template2.setName("doc2");
        template2.setAuthor("Author2");
        template2.setDate(LocalDate.now());
        return new Template[]{template1, template2};
    }

    @Test
    @WithMockUser(username = "admin")
    public void testIndexDocumentsWithAdmin() throws Exception {
        Document[] documents = getDocuments();
        given(this.documentRepository.findAll())
                .willReturn(Arrays.asList(documents));
        this.mvc.perform(get("/")).andExpect(status().isOk())
                .andExpect(model().attribute("documents",
                        Matchers.contains(documents)));
    }

    @Test
    @WithMockUser(username = "admin")
    public void testIndexTemplatesWithAdmin() throws Exception {
        Template[] templates = getTemplates();
        given(this.templateRepository.findAll())
                .willReturn(Arrays.asList(templates));
        this.mvc.perform(get("/")).andExpect(status().isOk())
                .andExpect(model().attribute("templates",
                        Matchers.contains(templates)));
    }

    @Test
    @WithMockUser(username = "user")
    public void testIndexTemplatesWithUser() throws Exception {
        Template[] templates = getTemplates();
        given(this.templateRepository.findAll())
                .willReturn(Arrays.asList(templates));
        this.mvc.perform(get("/")).andExpect(status().isOk())
                .andExpect(model().attribute("templates",
                        Matchers.nullValue()));
    }

    @Test
    @WithAnonymousUser
    public void WithAnonymousUser() throws Exception {
        this.mvc.perform(get("/")).andExpect(status().is3xxRedirection());
    }


}
