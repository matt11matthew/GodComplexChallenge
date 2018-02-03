package me.matthewe.challenge.utilities;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.material.Sign;

/**
 * Created by Matthew E on 2/3/2018.
 */
public class BlockUtilities {
    private final static BlockFace[] SIGN_BLOCK_FACES = new BlockFace[]{BlockFace.NORTH, BlockFace.WEST, BlockFace.EAST, BlockFace.SOUTH};


    public static Block getBlockBehindSign(Block signBlock) {
        if (signBlock.getType() == Material.WALL_SIGN) {
            Sign sign = (Sign) signBlock.getState().getData();
            return signBlock.getRelative(sign.getAttachedFace());
        }
        return null;
    }

    public static Block getSignAttachedToBlock(Block block) {
        for (BlockFace signBlockFace : SIGN_BLOCK_FACES) {
            Block relative = block.getRelative(signBlockFace);
            if (relative.getType() == Material.WALL_SIGN) {
                return relative;
            }
        }
        return null;
    }
}
