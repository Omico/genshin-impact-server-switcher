package me.omico.genshinimpact.serverswitcher.model

sealed class ClientType {

    object CN : ClientType()

    object Global : ClientType()

    object Unknown : ClientType()

    override fun toString(): String = javaClass.simpleName

    companion object {

        val types: Set<ClientType>
            get() = ClientType::class.sealedSubclasses
                .mapNotNull { it.objectInstance }
                .filter { it != Unknown }
                .toSet()
    }
}
