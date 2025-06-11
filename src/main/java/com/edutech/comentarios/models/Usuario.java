package com.edutech.comentarios.models;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "usuario")
@Data
public class Usuario {

    @Id
    @Column(name = "id_user")
    private Integer idUser;

    private String username;
    
    private String email;
}
