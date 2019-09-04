package com.hackerrank.github.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Actor implements Serializable {

    @Id
    private Long id;
    private String login;
    private String avatar_url;
    @OneToMany(mappedBy = "actor")
    @JsonIgnore
    public List<Event> events;

    public Actor() {
    }

    public Actor(Long id, String login, String avatar_url, List<Event> events) {
        this.id = id;
        this.login = login;
        this.avatar_url = avatar_url;
        this.events = events;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getAvatar_url() {
        return avatar_url;
    }

    public void setAvatar_url(String avatar_url) {
        this.avatar_url = avatar_url;
    }

    @JsonIgnore
    public List<Event> getEvents() {
        return events;
    }

    @JsonIgnore
    public int getTotalEvents() {
        return this.events.size();
    }

    @JsonIgnore
    public Timestamp getLastEvent() {
        if (!getEvents().isEmpty()) {
            getEvents().sort(Comparator.comparing(Event::getCreated_at).reversed());
            return getEvents().get(0).getCreated_at();
        }
        return Timestamp.from(Instant.MIN);
    }

    @JsonIgnore
    public int getMaximumStreak() {
        AtomicInteger currentStreak = new AtomicInteger(0);
        AtomicInteger maxStreak = new AtomicInteger(0);
        final AtomicReference<Timestamp> current = new AtomicReference<>(null);
        getEvents().stream().sorted(Comparator.comparing(Event::getEventDay)).forEachOrdered(event -> {
            if (current.get() == null) {
                current.set(event.getCreated_at());
            } else {
                if (event.getCreated_at().toLocalDateTime().minusDays(1).getDayOfYear() == current.get().toLocalDateTime().getDayOfYear()) {
                    currentStreak.incrementAndGet();
                    if (currentStreak.get() > maxStreak.get()) {
                        maxStreak.set(currentStreak.get());
                    }
                }
            }
        });

        return maxStreak.get();
    }

}
