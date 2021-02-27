package de.studiocode.miniatureblocks.resourcepack.model.part.impl

import de.studiocode.miniatureblocks.build.concurrent.ThreadSafeBlockData
import de.studiocode.miniatureblocks.resourcepack.model.element.Element
import de.studiocode.miniatureblocks.resourcepack.model.element.Texture
import de.studiocode.miniatureblocks.resourcepack.model.part.Part
import de.studiocode.miniatureblocks.resourcepack.texture.BlockTexture
import org.bukkit.Axis

class CrossPart(data: ThreadSafeBlockData) : Part() {
    
    private val texture = Texture(doubleArrayOf(0.0, 0.0, 1.0, 1.0), BlockTexture.of(data.material).textures[0])
    
    override val elements = listOf(CrossElement1(), CrossElement2())
    override val rotatable = false
    
    private inner class CrossElement1 :
        Element(doubleArrayOf(0.078125, 0.0, 0.0), doubleArrayOf(1.328125, 1.0, 0.0), texture) {
        
        init {
            setRotation(-45f, Axis.Y, 0.0, 0.0, 0.0)
        }
    }
    
    private inner class CrossElement2 :
        Element(doubleArrayOf(-0.328125, 0.0, 0.0), doubleArrayOf(0.921875, 1.0, 0.0), texture) {
        
        init {
            setRotation(45f, Axis.Y, 1.0, 0.0, 0.0)
        }
    }
    
}