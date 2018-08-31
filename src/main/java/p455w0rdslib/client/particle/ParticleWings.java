package p455w0rdslib.client.particle;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;

/**
 * @author p455w0rd
 *
 */
public class ParticleWings extends Particle {

	private float fadeTargetRed;
	private float fadeTargetGreen;
	private float fadeTargetBlue;
	private boolean fadingColor;
	private float baseAirFriction = 0.91F;

	public ParticleWings(World world, double x, double y, double z) {
		super(world, x, y, z, 0, 0, 0);
		motionX = 0;
		motionY = 0;
		motionZ = 0;
		particleScale *= 0.30F;
		particleMaxAge = 50 + rand.nextInt(12);
	}

	@Override
	public void onUpdate() {
		prevPosX = posX;
		prevPosY = posY;
		prevPosZ = posZ;

		if (particleAge++ >= particleMaxAge) {
			setExpired();
		}

		if (particleAge > particleMaxAge / 2) {
			setAlphaF(1.0F - ((float) particleAge - (float) (particleMaxAge / 2)) / particleMaxAge);

			if (fadingColor) {
				particleRed += (fadeTargetRed - particleRed) * 0.2F;
				particleGreen += (fadeTargetGreen - particleGreen) * 0.2F;
				particleBlue += (fadeTargetBlue - particleBlue) * 0.2F;
			}
		}

		setParticleTextureIndex(160 + (8 - 1 - particleAge * 8 / particleMaxAge));
		motionY -= 0.004F;
		move(motionX, motionY, motionZ);
		motionX *= baseAirFriction;
		motionY *= baseAirFriction;
		motionZ *= baseAirFriction;

		if (onGround) {
			motionX *= 0.699999988079071D;
			motionZ *= 0.699999988079071D;
		}

		if (particleAge < particleMaxAge / 2 && (particleAge + particleMaxAge) % 2 == 0) {
			ParticleWings particlefirework$spark = new ParticleWings(world, posX, posY, posZ);
			particlefirework$spark.setAlphaF(0.99F);
			particlefirework$spark.setRBGColorF(particleRed, particleGreen, particleBlue);
			particlefirework$spark.particleAge = particlefirework$spark.particleMaxAge / 2;
			Minecraft.getMinecraft().effectRenderer.addEffect(particlefirework$spark);
		}
	}

	@Override
	public void renderParticle(BufferBuilder buffer, Entity entityIn, float partialTicks, float rotationX, float rotationZ, float rotationYZ, float rotationXY, float rotationXZ) {
		if (entityIn.isInvisible()) {
			return;
		}
		if (particleAge < particleMaxAge / 3 || (particleAge + particleMaxAge) / 3 % 2 == 0) {
			super.renderParticle(buffer, entityIn, partialTicks, rotationX, rotationZ, rotationYZ, rotationXY, rotationXZ);
		}
	}

	@Override
	public boolean shouldDisableDepth() {
		return true;
	}

	@Override
	public int getBrightnessForRender(float p_189214_1_) {
		return 15728880;
	}

}
