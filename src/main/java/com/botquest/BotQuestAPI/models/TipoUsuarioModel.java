package com.botquest.BotQuestAPI.models;// Importa as anotações necessárias do JPA e Lombok
public enum TipoUsuarioModel {
    ADMIN("Admin"),
    COMUM("Comum");

    private String tipo;
    TipoUsuarioModel (String tipo) {
        this.tipo = tipo;
    }

    public String getRole() {
        return tipo;
    }
}
