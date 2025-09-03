package com.watchwise.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@Table(name = "user_list_item")
@Data
@NoArgsConstructor
public class UserListItem {

    @EmbeddedId
    private UserListItemId id;

    @Column(nullable = false)
    private Instant addedAt;
}
