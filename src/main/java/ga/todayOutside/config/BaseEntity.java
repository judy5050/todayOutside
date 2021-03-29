package ga.todayOutside.config;

import ga.todayOutside.src.notificationHistory.model.NotificationHistory;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@MappedSuperclass
public abstract class BaseEntity {
//    @Getter
//    @Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP", insertable = false, updatable = false)
//    @Temporal(TIMESTAMP)
    @CreationTimestamp
    @Column(name = "createdAt", nullable = false, updatable = false)
    private Date createdAt;

//    @Getter
//    @Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP", insertable = false, updatable = false)
//    @Temporal(TIMESTAMP)
    @UpdateTimestamp
    @Column(name = "updatedAt", nullable = false)
    private Date updatedAt;

    @Column(name = "isDeleted", nullable = false , columnDefinition = "char(1) default 'N'")
    private String isDeleted;

    @PrePersist
    public void prePersist() {
        this.isDeleted = this.isDeleted == null ? "N" : this.isDeleted;
    }



//    @PrePersist
//    void prePersist() {
//        this.createdAt = this.updatedAt = new Date();
//    }
//
//    @PreUpdate
//    void preUpdate() {
//        this.updatedAt = new Date();
//    }
}