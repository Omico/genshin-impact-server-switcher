package me.omico.genshinimpact.serverswitcher.model

import kotlinx.serialization.Serializable

@Serializable
data class PackageVersion(
    val remoteName: String,
    val md5: String,
    val fileSize: Int,
) {

    override fun hashCode(): Int {
        var result = fileSize
        result = 31 * result + md5.hashCode()
        result = 31 * result + remoteName.hashCode()
        return result
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as PackageVersion
        if (name != other.name) return false
        if (md5 != other.md5) return false
        if (fileSize != other.fileSize) return false
        return true
    }

    private val name: String
        get() = remoteName
            .replace("YuanShen_Data", "")
            .replace("GenshinImpact_Data", "")
}
