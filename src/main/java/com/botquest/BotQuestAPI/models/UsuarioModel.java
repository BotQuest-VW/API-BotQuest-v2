package com.botquest.BotQuestAPI.models;// Importa as anotações necessárias do JPA e Lombok
import com.botquest.BotQuestAPI.models.SetorModel;
import com.botquest.BotQuestAPI.models.TipoUsuarioModel;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serial;
import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.UUID;

// Marca a classe como uma entidade JPA
@Getter
@Setter
@Entity
// Especifica o nome da tabela no banco de dados
@Table(name = "tb_usuario")

public class UsuarioModel implements Serializable, UserDetails {
    @Serial
    private static final long serialVersion = 1L;
    // Define a chave primária da entidade
    @Id
    // Configura a estratégia de geração automática do ID
    @GeneratedValue(strategy = GenerationType.AUTO)
    // Especifica o nome da coluna no banco de dados
    @Column(name = "id", nullable = false)
    private UUID id;

    // Atributos da entidade
    private String nome;
    private String email;
    private String senha;
    private int vw_id;
    private String url_img;

    @Temporal(TemporalType.DATE)
    private Date data_nascimento;

    private TipoUsuarioModel tipo_usuario;

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL)
    @JsonBackReference
    private List<ChamadoModel> chamados;

    // Relacionamento de um para um com a entidade SetorModel
    @OneToOne
    @JoinColumn(name="id_setor", referencedColumnName = "id")
    private SetorModel setorModel;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if(this.tipo_usuario == TipoUsuarioModel.ADMIN){
            return List.of(
                    new SimpleGrantedAuthority("ROLE_ADMIN"),
                    new SimpleGrantedAuthority("ROLE_COMUM")
            );
        } else if (this.tipo_usuario == TipoUsuarioModel.COMUM) {
            return List.of(new SimpleGrantedAuthority("ROLE_COMUM"));
        }

        return null;
    }

    @Override
    public String getPassword() {
        return senha;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getSenha() {
        return senha;
    }
}
