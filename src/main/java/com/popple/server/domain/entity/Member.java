package com.popple.server.domain.entity;

import com.popple.server.domain.user.dto.UpdateMemberProfileRequestDto;
import com.popple.server.domain.user.vo.Role;
import com.popple.server.domain.user.vo.TokenPayload;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@DynamicInsert
public class Member extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    private String city;

    private String district;

    @Column(unique = true, nullable = false)
    private String nickname;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    @ColumnDefault(value = "'BASIC'")
    private LoginPlatform platform;


    //TODO 이후 디폴트 이미지를 정하고, 해당 이미지의 주소로 디폴트 값 변경 필요
    @ColumnDefault(value = "'profileDefaultImageUrl'")
    @Column(nullable = false)
    private String profileImgUrl;

    @ColumnDefault(value = "false")
    @Column(nullable = false)
    private Boolean inactive;

    public TokenPayload toPayload() {
        return TokenPayload.builder()
                .id(id)
                .role(Role.ROLE_USER)
                .platform(platform)
                .build();
    }

    public void updateProfile(UpdateMemberProfileRequestDto dto) {
        this.city = dto.getCity();
        this.district = dto.getDistrict();
        this.nickname = dto.getNickname();
        this.profileImgUrl = dto.getProfileImgUrl();
    }

    public void updatePassword(String newPassword) {
        this.password = newPassword;
    }

    public void setInactive() {
        this.inactive = true;
    }
}
