package com.yunus.foodlog.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.yunus.foodlog.converters.ListToStringConverter;
import lombok.Data;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "post")
@Data
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private User user;

    @Column(length = 60)
    private String title;

    @Column(columnDefinition = "text", length = 140)
    private String text;

    // TODO: Max 20 Images
    // https://www.baeldung.com/java-hibernate-map-postgresql-array => another way of mapping.
    @Column(name = "image_paths", length = 9999)
    @Convert(converter = ListToStringConverter.class)
    private List<String> imagePaths;

    @Column(name = "short_video_path", length = 333)
    private String shortVideoPath;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;
}
