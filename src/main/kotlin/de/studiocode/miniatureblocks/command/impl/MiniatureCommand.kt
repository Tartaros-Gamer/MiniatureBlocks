package de.studiocode.miniatureblocks.command.impl

import com.mojang.brigadier.arguments.FloatArgumentType
import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.context.CommandContext
import de.studiocode.miniatureblocks.MiniatureBlocks
import de.studiocode.miniatureblocks.command.PlayerCommand
import de.studiocode.miniatureblocks.resourcepack.model.BuildDataModelParser
import de.studiocode.miniatureblocks.utils.getTargetMiniature

class MiniatureCommand(name: String, permission: String) : PlayerCommand(name, permission) {

    private val namePattern = "[A-Za-z0-9]*".toRegex()
    
    init {
        command = command
                .then(literal("create")
                        .then(argument<String>("name", StringArgumentType.string())
                                .executes { handleCreateCommand(it); 0}))
                .then(literal("autorotate")
                        .then(argument<Float>("degreesPerTick", FloatArgumentType.floatArg())
                                .executes { handleAutoRotateCommand(it); 0}))
                .then(literal("rotation")
                        .then(argument<Float>("degrees", FloatArgumentType.floatArg())
                                .executes { handleRotateCommand(it); 0 }))
    }

    private fun handleCreateCommand(context: CommandContext<Any>) {
        val player = getPlayer(context.source)
        
        if (name.matches(namePattern)) {
            val resourcePack = MiniatureBlocks.INSTANCE.resourcePack
            if (!resourcePack.hasModel(name)) {
                val builderWorld = MiniatureBlocks.INSTANCE.builderWorld
                if (builderWorld.isPlayerInValidBuildArea(player)) {
                    val buildData = builderWorld.getBuildData(player)
                    val parser = BuildDataModelParser(buildData)
                    val modelData = parser.parse()
                    resourcePack.addNewModel(name, modelData)

                    player.sendMessage("§7A new model has been created.")
                } else player.sendMessage("§cYou're not in a build area.")
            } else player.sendMessage("§cA model with that name already exists.")
        } else player.sendMessage("§cName does not match pattern $namePattern")
    }

    private fun handleAutoRotateCommand(context: CommandContext<Any>) {
        val player = getPlayer(context.source)
        val degreesPerTick = context.getArgument("degreesPerTick", Float::class.java)
        
        val miniature = player.getTargetMiniature()
        if (miniature != null) {
            MiniatureBlocks.INSTANCE.miniatureManager.setMiniatureAutoRotate(miniature, degreesPerTick)
        } else {
            player.sendMessage("§cPlease look at the miniature you want to rotate and try again.")
        }
    }

    private fun handleRotateCommand(context: CommandContext<Any>) {
        val player = getPlayer(context.source)
        val degrees = context.getArgument("degrees", Float::class.java)

        val miniature = player.getTargetMiniature()
        if (miniature != null) {
            MiniatureBlocks.INSTANCE.miniatureManager.rotateMiniature(miniature, degrees)
        } else {
            player.sendMessage("§cPlease look at the miniature you want to rotate and try again.")
        }
    }
    
}
