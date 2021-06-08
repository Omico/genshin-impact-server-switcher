package me.omico.genshinimpact.serverswitcher.model

sealed class Progress(
    open val message: String,
    val isFailure: Boolean,
) {

    data class Processing(
        override val message: String
    ) : Progress(
        message = message,
        isFailure = false,
    )

    data class Success(
        override val message: String
    ) : Progress(
        message = message,
        isFailure = false,
    )

    data class Failure(
        override val message: String
    ) : Progress(
        message = message,
        isFailure = true,
    )
}
