package com.edutech.comentarios.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.edutech.comentarios.models.Comentario;

@Repository
public interface ComentarioRepository extends JpaRepository<Comentario, Integer>
{
    //Métodos para poder encontrar comentarios tanto por id de curso como por id de usuario
    List<Comentario> findByCursoIdCurso(Integer cursoId);
    List<Comentario> findByUsuarioIdUser(Integer usuarioId);

}
