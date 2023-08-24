package com.popple.server.domain.entity;

import com.popple.server.domain.board.dto.PostReqDto;
import lombok.*;

import javax.persistence.*;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
public class Post extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private Member member;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    public void modifyPost(PostReqDto postReqDto){
        title = postReqDto.getTitle();
        content = postReqDto.getContent();
    }
}
