package com.planetsystems.tela.api.ClockInOutConsumer.model.enums;

public enum RolloutPhase {

	PILOT("Pilot"), SCALE_UP("Scale Up"), ROLL_OUT("Rollout 1"), NATION_ROLL_OUT("National Rollout"),;

	private String phase;

	RolloutPhase(String phase) {
		this.phase = phase;
	}

	public String getRolloutPhase() {
		return phase;
	}

	public void setRolloutPhase(String phase) {
		this.phase = phase;
	}

	public static RolloutPhase getRolloutPhase(String rolloutPhase) {
		for (RolloutPhase phase : RolloutPhase.values()) {
			if (phase.getRolloutPhase().equalsIgnoreCase(rolloutPhase)) {
				return phase;
			}
		}
		return null;
	}
}
