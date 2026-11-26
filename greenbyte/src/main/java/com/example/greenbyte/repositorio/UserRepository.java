package com.example.greenbyte.repositorio;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.Lock;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.example.greenbyte.models.User;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByCorreo(String correo);

    @Modifying
    @Transactional
    @Query("update User u set u.nombre = :nombre, u.telefono = :telefono, u.acceso = :acceso where u.id = :id")
    int actualizarDatos(@Param("id") Long id, @Param("nombre") String nombre,
                        @Param("telefono") String telefono, @Param("acceso") Character acceso);
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select u from User u where u.correo = :correo")
    Optional<User> buscarClienteParaVenta(@Param("correo") String correo);
}
