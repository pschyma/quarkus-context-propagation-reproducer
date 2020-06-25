package com.example

import org.eclipse.microprofile.context.ManagedExecutor
import javax.inject.Inject
import javax.sql.DataSource
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType

@Path("/hello")
class ExampleResource @Inject constructor(private val executor: ManagedExecutor) {

	@GET
	@Produces(MediaType.TEXT_PLAIN)
	fun hello(): String {
		return "hello world"
	}
}
