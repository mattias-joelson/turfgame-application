package org.joelson.turf.application.db;

import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "visits", uniqueConstraints = @UniqueConstraint(columnNames = { "take_id", "user_id" }))
public class VisitEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @ManyToOne(optional = false)
    private TakeEntity take;

    @Id
    @ManyToOne(optional = false)
    private UserEntity user;

    @Enumerated
    private VisitType type;

    public VisitEntity() {
    }

    public static VisitEntity build(TakeEntity take, UserEntity user, VisitType type) {
        VisitEntity visit = new VisitEntity();
        visit.setTake(take);
        visit.setUser(user);
        visit.setType(type);
        return visit;
    }

    public TakeEntity getTake() {
        return take;
    }

    public void setTake(TakeEntity take) {
        this.take = Objects.requireNonNull(take, "Take can not be null!");
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = Objects.requireNonNull(user, "User can not be null!");
    }

    public VisitType getType() {
        return type;
    }

    public void setType(VisitType type) {
        this.type = Objects.requireNonNull(type, "Type can not be null!");
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof VisitEntity that) {
            return Objects.equals(take, that.take) && Objects.equals(user, that.user) && type == that.type;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(take, user, type);
    }

    @Override
    public String toString() {
        return String.format("VisitEntity[zone %s, when %s, user %s, type %s]",
                take.getZone(), take.getWhen(), EntityUtil.toStringPart(user), type);
    }
}
