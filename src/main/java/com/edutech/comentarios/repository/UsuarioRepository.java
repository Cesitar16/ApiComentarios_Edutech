package com.edutech.comentarios.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.edutech.comentarios.models.Usuario;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer>
{

}
