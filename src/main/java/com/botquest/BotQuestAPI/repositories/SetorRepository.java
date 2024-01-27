package com.botquest.BotQuestAPI.repositories;

import com.botquest.BotQuestAPI.models.SetorModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SetorRepository extends JpaRepository<SetorModel, UUID> {
}