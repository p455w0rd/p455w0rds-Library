package p455w0rdslib.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

/**
 * @author p455w0rd
 *
 */
public class ModelContribDankNull extends ModelBase {

	ModelRenderer post4;
	ModelRenderer post1;
	ModelRenderer post2;
	ModelRenderer post3;
	ModelRenderer basebottom;
	ModelRenderer bartop2;
	ModelRenderer bartop1;
	ModelRenderer window1;
	ModelRenderer window2;
	ModelRenderer window3;
	ModelRenderer window4;
	ModelRenderer bartop3;
	ModelRenderer bartop4;

	public ModelContribDankNull() {
		textureWidth = 64;
		textureHeight = 64;
		buildModel();
	}

	private void buildModel() {

		post1 = new ModelRenderer(this, 0, 0);
		post1.addBox(0F, 0F, 0F, 3, 13, 3);
		post1.setRotationPoint(0F, -13F, 0F);
		//post1.setTextureSize(64, 64);
		//post1.mirror = true;
		setRotation(post1, 0F, 0F, 0F);
		post2 = new ModelRenderer(this, 0, 0);
		post2.addBox(0F, 0F, 0F, 3, 13, 3);
		post2.setRotationPoint(13F, -13F, 0F);
		//post2.setTextureSize(64, 64);
		//post2.mirror = true;
		setRotation(post2, 0F, 0F, 0F);
		post3 = new ModelRenderer(this, 0, 0);
		post3.addBox(0F, 0F, 0F, 3, 13, 3);
		post3.setRotationPoint(0F, -13F, 13F);
		//post3.setTextureSize(64, 64);
		//post3.mirror = true;
		setRotation(post3, 0F, 0F, 0F);
		post4 = new ModelRenderer(this, 0, 0);
		post4.addBox(0F, 0F, 0F, 3, 13, 3);
		post4.setRotationPoint(13F, -13F, 13F);
		//post4.setTextureSize(64, 64);
		//post4.mirror = true;
		setRotation(post4, 0F, 0F, 0F);

		bartop1 = new ModelRenderer(this, 13, 0);
		bartop1.addBox(0F, 0F, 0F, 10, 3, 3);
		bartop1.setRotationPoint(3F, -13F, 13F);
		//bartop1.setTextureSize(64, 64);
		//bartop1.mirror = true;
		setRotation(bartop1, 0F, 0F, 0F);
		bartop2 = new ModelRenderer(this, 13, 0);
		bartop2.addBox(0F, 0F, 0F, 10, 3, 3);
		bartop2.setRotationPoint(3F, -13F, 0F);
		//bartop2.setTextureSize(64, 64);
		//bartop2.mirror = true;
		setRotation(bartop2, 0F, 0F, 0F);
		bartop3 = new ModelRenderer(this, 13, 0);
		bartop3.addBox(0F, 0F, 0F, 10, 3, 3);
		bartop3.setRotationPoint(3F, -13F, 3F);
		//bartop3.setTextureSize(64, 64);
		//bartop3.mirror = true;
		setRotation(bartop3, 0F, -1.570796F, 0F);
		bartop4 = new ModelRenderer(this, 13, 0);
		bartop4.addBox(0F, 0F, 0F, 10, 3, 3);
		bartop4.setRotationPoint(16F, -13F, 3F);
		//bartop4.setTextureSize(64, 64);
		//bartop4.mirror = true;
		setRotation(bartop4, 0F, -1.570796F, 0F);

		window1 = new ModelRenderer(this, 0, 37);
		window1.addBox(0F, 0F, 0F, 1, 10, 10);
		window1.setRotationPoint(13F, -10F, 14F);
		//window1.setTextureSize(64, 64);
		//window1.mirror = true;
		setRotation(window1, 0F, -1.570796F, 0F);
		window2 = new ModelRenderer(this, 0, 37);
		window2.addBox(0F, 0F, 0F, 1, 10, 10);
		window2.setRotationPoint(1F, -10F, 3F);
		//window2.setTextureSize(64, 64);
		//window2.mirror = true;
		setRotation(window2, 0F, 0F, 0F);
		window3 = new ModelRenderer(this, 0, 37);
		window3.addBox(0F, 0F, 0F, 1, 10, 10);
		window3.setRotationPoint(14F, -10F, 3F);
		//window3.setTextureSize(64, 64);
		//window3.mirror = true;
		setRotation(window3, 0F, 0F, 0F);
		window4 = new ModelRenderer(this, 0, 37);
		window4.addBox(0F, 0F, 0F, 1, 10, 10);
		window4.setRotationPoint(13, -10F, 1F);
		//window4.setTextureSize(64, 64);
		//window4.mirror = true;
		setRotation(window4, 0F, -1.570796F, 0F);

		basebottom = new ModelRenderer(this, 0, 17);
		basebottom.addBox(0F, 0F, 0F, 16, 3, 16);
		basebottom.setRotationPoint(0F, 0F, 0F);
		//basebottom.setTextureSize(64, 64);
		//basebottom.mirror = true;
		setRotation(basebottom, 0F, 0F, 0F);

		basebottom.addChild(post1);
		basebottom.addChild(post2);
		basebottom.addChild(post3);
		basebottom.addChild(post4);
		basebottom.addChild(bartop1);
		basebottom.addChild(bartop2);
		basebottom.addChild(bartop3);
		basebottom.addChild(bartop4);
		basebottom.addChild(window1);
		basebottom.addChild(window2);
		basebottom.addChild(window3);
		basebottom.addChild(window4);

	}

	@Override
	public void render(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
		basebottom.render(scale);
		//buildModel();
		//renderPart(basebottom, netHeadYaw, headPitch, scale);
	}

	private void renderPart(ModelRenderer part, float x, float y, float scale) {
		part.rotateAngleY = x / (180F / (float) Math.PI);
		part.rotateAngleX = y / (180F / (float) Math.PI);
		part.render(scale);
	}

	public void setRotation(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
	}

}
