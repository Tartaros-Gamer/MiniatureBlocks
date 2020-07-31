package de.studiocode.miniatureblocks.utils

import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import de.studiocode.miniatureblocks.utils.ReflectionUtils.getCB
import de.studiocode.miniatureblocks.utils.ReflectionUtils.getCBClass
import de.studiocode.miniatureblocks.utils.ReflectionUtils.getMethod
import de.studiocode.miniatureblocks.utils.ReflectionUtils.getNMS
import de.studiocode.miniatureblocks.utils.ReflectionUtils.getNMSClass
import org.bukkit.Bukkit

@Suppress("MemberVisibilityCanBePrivate")
object ReflectionRegistry {

    // NMS & CB paths
    val NMS_PACKAGE_PATH = getNMS()
    val CB_PACKAGE_PATH = getCB()

    // NMS classes
    val NMS_MINECRAFT_SERVER_CLASS = getNMSClass("MinecraftServer")
    val NMS_DEDICATED_SERVER_CLASS = getNMSClass("DedicatedServer")
    val NMS_COMMAND_DISPATCHER_CLASS = getNMSClass("CommandDispatcher")
    val NMS_COMMAND_LISTENER_WRAPPER_CLASS = getNMSClass("CommandListenerWrapper")
    val NMS_ENTITY_PLAYER_CLASS = getNMSClass("EntityPlayer")
    val NMS_PLAYER_LIST_CLASS = getNMSClass("PlayerList")

    // CB classes
    val CB_CRAFT_SERVER_CLASS = getCBClass("CraftServer")
    val CB_CRAFT_PLAYER_CLASS = getCBClass("entity.CraftPlayer")

    // NMS methods
    val NMS_DEDICATED_SERVER_GET_COMMAND_DISPATCHER_METHOD = getMethod(NMS_DEDICATED_SERVER_CLASS, false, "getCommandDispatcher")
    val NMS_COMMAND_DISPATCHER_GET_BRIGADIER_COMMAND_DISPATCHER_METHOD = getMethod(NMS_COMMAND_DISPATCHER_CLASS, false, "a")
    val NMS_COMMAND_LISTENER_WRAPPER_GET_ENTITY_METHOD = getMethod(NMS_COMMAND_LISTENER_WRAPPER_CLASS, false, "getEntity")
    val NMS_MINECRAFT_SERVER_GET_PLAYER_LIST_METHOD = getMethod(NMS_MINECRAFT_SERVER_CLASS, false, "getPlayerList")
    val NMS_PLAYER_LIST_UPDATE_PERMISSION_LEVEL_METHOD = getMethod(NMS_PLAYER_LIST_CLASS, false, "d", NMS_ENTITY_PLAYER_CLASS)
    
    // CB methods
    val CB_CRAFT_SERVER_GET_SERVER_METHOD = getMethod(CB_CRAFT_SERVER_CLASS, false, "getServer")
    val CB_CRAFT_PLAYER_GET_HANDLE_METHOD = getMethod(CB_CRAFT_PLAYER_CLASS, false, "getHandle")
    
    // other methods
    val COMMAND_DISPATCHER_REGISTER_METHOD = getMethod(CommandDispatcher::class.java, false, "register", LiteralArgumentBuilder::class.java)
    
    // objects
    val NMS_DEDICATED_SERVER = CB_CRAFT_SERVER_GET_SERVER_METHOD.invoke(Bukkit.getServer())!!
    val NMS_COMMAND_DISPATCHER = NMS_DEDICATED_SERVER_GET_COMMAND_DISPATCHER_METHOD.invoke(NMS_DEDICATED_SERVER)!!
    val COMMAND_DISPATCHER = NMS_COMMAND_DISPATCHER_GET_BRIGADIER_COMMAND_DISPATCHER_METHOD.invoke(NMS_COMMAND_DISPATCHER)!!

}