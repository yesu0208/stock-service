package arile.toy.stock_service.domain;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.ZonedDateTime;

@Getter
@ToString
@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
public abstract class AuditingFields {

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) // 표준 date로 변경 (ISO)
    @CreatedDate // 자동으로 setting
    @Column(nullable = false, updatable = false) // 생성과 관련된 정보는 update 되어서는 안된다.
    protected ZonedDateTime createdAt; // protected : 자식 class가 참조할 수 있도록 private(x)

    @CreatedBy
    @Column(nullable = false, updatable = false)
    protected String createdBy;

    @Setter
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @LastModifiedDate
    @Column(nullable = false)
    protected ZonedDateTime modifiedAt;

    @Setter
    @LastModifiedBy
    @Column(nullable = false)
    protected String modifiedBy;
}