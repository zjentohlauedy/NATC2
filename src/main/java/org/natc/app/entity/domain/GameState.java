package org.natc.app.entity.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity(name = "gamestate_t")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GameState {
	@Id
	private Integer gameId;
	private Integer started;
	private Integer startTime;
	private Integer sequence;
	private Integer period;
	private Integer overtime;
	private Integer timeRemaining;
	private Integer clockStopped;
	private Integer possession;
	private String lastEvent;
}
