package org.natc.app.entity.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;

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
