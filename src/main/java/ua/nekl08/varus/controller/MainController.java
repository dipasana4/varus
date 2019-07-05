package ua.nekl08.varus.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import ua.nekl08.varus.model.Document;
import ua.nekl08.varus.model.Template;
import ua.nekl08.varus.repository.DocumentRepository;
import ua.nekl08.varus.repository.TemplateRepository;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;

@Controller
@RequestMapping(path = "/")
public class MainController {

    @Autowired
    private TemplateRepository templateRepository;
    @Autowired
    private DocumentRepository documentRepository;

    @GetMapping("/login*")
    public String login() {
        return "login";
    }

    @GetMapping("/")
    public String index(Model model, Authentication auth) {

        Iterable<Document> documents = documentRepository.findAll();
        model.addAttribute("documents", documents);

        for (GrantedAuthority authority : auth.getAuthorities()) {
            if (authority.toString().equals("ROLE_ADMIN")) {
                Iterable<Template> templates = templateRepository.findAll();
                model.addAttribute("templates", templates);
                return "indexAdmin";
            }
        }

        return "indexUser";
    }

    @PostMapping("/template/delete")
    public String deleteTemplate(@RequestParam("templateId") Integer id, Model model) {
        templateRepository.deleteById(id.longValue());
        return "redirect:/";
    }

    @GetMapping("/template/{id}")
    public String template(@PathVariable Integer id, Model model) {
        Template template = templateRepository.findById(id.longValue()).get();
        model.addAttribute("template", template);
        return "template";
    }

    @GetMapping("/template/create")
    public String createTemplate(Model model) {
        Template template = new Template();
        template.setDate(LocalDate.now());
        model.addAttribute("template", template);
        return "template";
    }

    @PostMapping(value = "/template/create", params = {"save"})
    public String saveSeedstarter(final Template seedStarter, final BindingResult bindingResult, final ModelMap model) {
        this.templateRepository.save(seedStarter);
        model.clear();
        return "redirect:/";
    }

    @PostMapping(value = "/template/create", params = {"addRow"})
    public String addRow(final Template seedStarter, final BindingResult bindingResult) {
        seedStarter.getFields().add("новое поле №" + (seedStarter.getFields().size() + 1));
        return "template";
    }

    @PostMapping(value = "/template/create", params = {"removeRow"})
    public String removeRow(final Template seedStarter, final BindingResult bindingResult, final HttpServletRequest req) {
        final Integer rowId = Integer.valueOf(req.getParameter("removeRow"));
        seedStarter.getFields().remove(rowId.intValue());
        return "template";
    }

    @PostMapping("/document/delete")
    public String deleteDocument(@RequestParam("documentId") Integer id, Model model) {
        documentRepository.deleteById(id.longValue());
        return "redirect:/";
    }

    @GetMapping("/document/{id}")
    public String document(@PathVariable Integer id, Model model, HttpServletRequest request) {
        Document document = documentRepository.findById(id.longValue()).get();
        model.addAttribute("document", document);
        String qrcode = "https://api.qrserver.com/v1/create-qr-code/?size=150x150&data=" + request.getRequestURL();
        model.addAttribute("qrcode", qrcode);

        return "document";
    }

    @GetMapping("/document/create/{id}")
    public String createDocument(@PathVariable Integer id, Model model) {
        Template template = templateRepository.findById(id.longValue()).get();
        Document document = new Document();
        template.getFields().forEach(s -> document.getFieldAndValue().put(s, ""));
        document.setType(template.getName());
        document.setDate(LocalDate.now());
        model.addAttribute("document", document);
        model.addAttribute("file", template.isEnableFieldFile());
        model.addAttribute("term", template.isEnableFieldTerm());
        model.addAttribute("url", template.isEnableFieldUrl());
        return "createdocument";
    }

    @PostMapping("/document/create")
    public String saveDocument(Document document, @RequestParam("docFile") MultipartFile file) throws IOException {
        if (!file.isEmpty()) {
            document.setFileName(file.getOriginalFilename());
            document.setFile(file.getBytes());
        }
        documentRepository.save(document);
        return "redirect:/document/" + document.getId();
    }


    @GetMapping("/document/edit/{id}")
    public String editDocument(@PathVariable Integer id, Model model) {
        Document document = documentRepository.findById(id.longValue()).get();
        model.addAttribute("document", document);
        model.addAttribute("file", document.getFile() != null);
        model.addAttribute("term", document.getTermYear() != null);
        model.addAttribute("url", document.getUrl() != null);
        return "createdocument";
    }

    @GetMapping("/files/{id}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable Integer id) {
        Document document = documentRepository.findById(id.longValue()).get();
        HttpHeaders httpHeaders = new HttpHeaders();
        ContentDisposition contentDisposition = ContentDisposition.builder("attachment")
                .filename(document.getFileName(), StandardCharsets.UTF_8)
                .build();
        httpHeaders.setContentDisposition(contentDisposition);

        return new ResponseEntity<>(new ByteArrayResource(document.getFile()),
                httpHeaders, HttpStatus.OK);
    }

    // можно создать свою иерархию исключение, под разные случаи например DocumetNotFoundExeption
    @ExceptionHandler(Exception.class)
    public ModelAndView handleError(HttpServletRequest req, Exception ex) {
//        logger.error("Request: " + req.getRequestURL() + " raised " + ex);
        ModelAndView mav = new ModelAndView();
        mav.addObject("exception", ex);
        mav.addObject("url", req.getRequestURL());
        mav.setViewName("error");
        return mav;
    }
}
