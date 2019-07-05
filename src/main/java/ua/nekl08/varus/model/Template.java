package ua.nekl08.varus.model;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
public class Template {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(unique = true, nullable = false, length = 100)
    private String name;

    @NotNull
    @Column(nullable = false, length = 100)
    private String author;

    @NotNull
    @Column(nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;

    private boolean enableFieldTerm;

    private boolean enableFieldFile;

    private boolean enableFieldUrl;

    @ElementCollection(fetch = FetchType.EAGER)
//    private Set<String> fields = new HashSet<>();
    private List<String> fields = new ArrayList<>();

//-------------Get and SET----------------
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public boolean isEnableFieldTerm() {
        return enableFieldTerm;
    }

    public void setEnableFieldTerm(boolean enableFieldTerm) {
        this.enableFieldTerm = enableFieldTerm;
    }

    public boolean isEnableFieldFile() {
        return enableFieldFile;
    }

    public void setEnableFieldFile(boolean enableFieldFile) {
        this.enableFieldFile = enableFieldFile;
    }

    public boolean isEnableFieldUrl() {
        return enableFieldUrl;
    }

    public void setEnableFieldUrl(boolean enableFieldUrl) {
        this.enableFieldUrl = enableFieldUrl;
    }

//    public Set<String> getFields() {
//        return fields;
//    }
//
//    public void setFields(Set<String> fields) {
//        this.fields = fields;
//    }


    public List<String> getFields() {
        return fields;
    }

    public void setFields(List<String> fields) {
        this.fields = fields;
    }
}
