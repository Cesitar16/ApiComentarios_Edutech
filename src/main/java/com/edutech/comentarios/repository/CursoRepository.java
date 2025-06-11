package com.edutech.comentarios.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.edutech.comentarios.models.Curso;

@Repository
public interface CursoRepository extends JpaRepository<Curso, Integer>
{

}
