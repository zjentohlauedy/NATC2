package org.natc.app.entity.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GameState {
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
