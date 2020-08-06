package de.studiocode.miniatureblocks.utils

import com.mojang.brigadier.builder.LiteralArgumentBuilder
import de.studiocode.miniatureblocks.utils.ReflectionRegistry.CB_CRAFT_PLAYER_GET_HANDLE_METHOD
import de.studiocode.miniatureblocks.utils.ReflectionRegistry.CB_CRAFT_WORLD_ADD_ENTITY_METHOD
import de.studiocode.miniatureblocks.utils.ReflectionRegistry.CB_CRAFT_WORLD_CREATE_ENTITY_METHOD
import de.studiocode.miniatureblocks.utils.ReflectionRegistry.CB_PACKAGE_PATH
import de.studiocode.miniatureblocks.utils.ReflectionRegistry.NMS_COMMAND_LISTENER_WRAPPER_GET_ENTITY_METHOD
import de.studiocode.miniatureblocks.utils.ReflectionRegistry.NMS_DEDICATED_SERVER
import de.studiocode.miniatureblocks.utils.ReflectionRegistry.NMS_ENTITY_GET_BUKKIT_ENTITY_METHOD
import de.studiocode.miniatureblocks.utils.ReflectionRegistry.NMS_MINECRAFT_SERVER_GET_PLAYER_LIST_METHOD
import de.studiocode.miniatureblocks.utils.ReflectionRegistry.NMS_PACKAGE_PATH
import de.studiocode.miniatureblocks.utils.ReflectionRegistry.NMS_PLAYER_LIST_UPDATE_PERMISSION_LEVEL_METHOD
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.World
import org.bukkit.entity.Entity
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import org.bukkit.event.entity.CreatureSpawnEvent
import java.lang.reflect.Constructor
import java.lang.reflect.Field
import java.lang.reflect.Method

@Suppress("MemberVisibilityCanBePrivate")
object ReflectionUtils {

    fun getNMS(): String {
        val path = Bukkit.getServer().javaClass.getPackage().name
        val version = path.substring(path.lastIndexOf(".") + 1)
        return "net.minecraft.server.$version."
    }

    fun getCB(): String {
        val path = Bukkit.getServer().javaClass.getPackage().name
        val version = path.substring(path.lastIndexOf(".") + 1)
        return "org.bukkit.craftbukkit.$version."
    }

    fun getNMS(name: String): String? {
        return NMS_PACKAGE_PATH + name
    }

    fun getCB(name: String): String? {
        return CB_PACKAGE_PATH + name
    }

    fun getNMSClass(name: String): Class<*> {
        return Class.forName(getNMS(name))
    }

    fun getCBClass(name: String): Class<*> {
        return Class.forName(getCB(name))
    }

    fun getMethod(clazz: Class<*>, declared: Boolean, methodName: String, vararg args: Class<*>): Method {
        return if (declared) clazz.getDeclaredMethod(methodName, *args) else clazz.getMethod(methodName, *args)
    }

    fun getConstructor(clazz: Class<*>, declared: Boolean, vararg args: Class<*>): Constructor<*> {
        return if (declared) clazz.getDeclaredConstructor(*args) else clazz.getConstructor(*args)
    }

    fun getField(clazz: Class<*>, declared: Boolean, name: String): Field {
        val field = if (declared) clazz.getDeclaredField(name) else clazz.getField(name)
        if (declared) field.isAccessible = true
        return field
    }

    fun registerCommand(builder: LiteralArgumentBuilder<*>) {
        ReflectionRegistry.COMMAND_DISPATCHER_REGISTER_METHOD.invoke(ReflectionRegistry.COMMAND_DISPATCHER, builder)
    }

    fun getEntityPlayer(player: Player): Any {
        return CB_CRAFT_PLAYER_GET_HANDLE_METHOD.invoke(player)
    }

    fun getPlayerFromEntityPlayer(entityPlayer: Any): Player? {
        return Bukkit.getOnlinePlayers().find { getEntityPlayer(it) == entityPlayer }
    }

    fun getEntityFromCommandListenerWrapper(commandListenerWrapper: Any): Any? =
            NMS_COMMAND_LISTENER_WRAPPER_GET_ENTITY_METHOD.invoke(commandListenerWrapper)

    fun getPlayerFromCommandListenerWrapper(commandListenerWrapper: Any): Player? {
        val entity = getEntityFromCommandListenerWrapper(commandListenerWrapper)
        return if (entity != null) getPlayerFromEntityPlayer(entity) else null
    }
    
    fun updatePermissionLevel(entityPlayer: Any) {
        val playerList = NMS_MINECRAFT_SERVER_GET_PLAYER_LIST_METHOD.invoke(NMS_DEDICATED_SERVER)
        NMS_PLAYER_LIST_UPDATE_PERMISSION_LEVEL_METHOD.invoke(playerList, entityPlayer)
    }
    
    fun updatePermissionLevelPlayer(player: Player) {
        updatePermissionLevel(getEntityPlayer(player))
    }
    
    fun createNMSEntity(world: World, location: Location, entityType: EntityType): Any {
        return CB_CRAFT_WORLD_CREATE_ENTITY_METHOD.invoke(world, location, entityType.entityClass)
    }
    
    fun getBukkitEntityFromNMSEntity(entity: Any): Entity {
        return NMS_ENTITY_GET_BUKKIT_ENTITY_METHOD.invoke(entity) as Entity
    }
    
    fun addNMSEntityToWorld(world: World, entity: Any): Entity {
        return CB_CRAFT_WORLD_ADD_ENTITY_METHOD.invoke(world, entity, CreatureSpawnEvent.SpawnReason.CUSTOM, null) as Entity
    }

}