package com.snorlax.domain;

public enum AccountStatus {

	PENDING_VERIFICATION, 	// Account is created but not yet verified
	ACTIVE, 				// Account is created and in good standing
	SUSPENDED, 				// Account is temporary suspended, possibly due to violation
	DEACTIVATED, 			// Account is deactivated, user may have chosen to deactivate it
	BANNED, 				// Account is permanently banned due to severe violation
	CLOSED  				// Account is permanently close, possibly at user request
	
}
