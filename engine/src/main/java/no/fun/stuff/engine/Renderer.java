package no.fun.stuff.engine;

import java.awt.image.DataBufferInt;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import no.fun.stuff.engine.game.objects.Rect;
import no.fun.stuff.engine.game.geo.triangle.TriangleRendererReversed;
import no.fun.stuff.engine.game.geo.triangle.TriangleRendererReversedTexture;
import no.fun.stuff.engine.game.objects.Rect4PointRenderer;
import no.fun.stuff.engine.game.texturemap.Texture2d;
import no.fun.stuff.engine.game.texturemap.Texturemap4Points;
import no.fun.stuff.engine.gfx.Font;
import no.fun.stuff.engine.gfx.Image;
import no.fun.stuff.engine.gfx.ImageRequest;
import no.fun.stuff.engine.gfx.ImageTile;
import no.fun.stuff.engine.gfx.Light;
import no.fun.stuff.engine.gfx.LightRequest;
import no.fun.stuff.engine.matrix.Vector2D;

public class Renderer {
    private final Texturemap4Points drawTexture = new Texturemap4Points();
    private final Rect4PointRenderer drawFilledRect = new Rect4PointRenderer();
    private final TriangleRendererReversed triangleRenderer;
    private final TriangleRendererReversedTexture textureTriangleRenderer;
    private List<ImageRequest> imageRequests = new ArrayList<>();
    private List<LightRequest> lightRequest = new ArrayList<>();
    private Font font = Font.STANDARD;
    private int pW, pH;
    private int[] p;
    private int[] zb;
    private int[] lm;
    private int[] lb;
    private int ambientcolor = 0xffffffff;
    private int zDept = 0;
    private boolean processing = false;

    public Renderer(GameContainer container) {
        pW = container.getWith();
        pH = container.getHeight();
        p = ((DataBufferInt) container.getWindow().getImage().getRaster().getDataBuffer()).getData();
        zb = new int[p.length];
        lm = new int[p.length];
        lb = new int[p.length];
        triangleRenderer = new TriangleRendererReversed(this);
        textureTriangleRenderer = new TriangleRendererReversedTexture(this);
    }
    public void drawCircle(Vector2D center, float radius, int color) {
        float positiveRadius = Math.abs(radius);
        float radius2 = positiveRadius * positiveRadius;
        float x = -positiveRadius;
        int ra = (int) positiveRadius;
        for (int xx = -ra; xx <= 0; x += 1.0f, xx++) {
                int y = (int) Math.sqrt(radius2 - x * x);
            float y1 = center.getY();
            int thePlussx = (int)(center.getX() + xx);
            int theMinusx = (int)(center.getX() - xx);
            for (int i = -y; i <= y; i++) {
                int ypos = (int) (y1 + i);
                setPixel(thePlussx, ypos, color);
                setPixel(theMinusx, ypos, color);
            }
        }
    }

    public void textureTriangle(final Vector2D p1, final Vector2D p2, final Vector2D p3,
                                final Vector2D uv1, final Vector2D uv2, final Vector2D uv3, Image texture) {
        textureTriangleRenderer.drawTriangle(p1, p2, p3, uv1, uv2, uv3, texture);
    }
    public void fillTriangle(Vector2D p1, Vector2D p2, Vector2D p3, int color) {
        triangleRenderer.drawTriangle2(p1, p2, p3, color);
    }

    public void drawRect4(final Rect r) {
        drawFilledRect.drawRect(r.getWordCordinate(), this);
    }

    public void drawTexture(final Texture2d face, final Image texture) {
        drawTexture.texturemap(face, texture, this);
    }

    public void clear() {
        for (int i = 0; i < p.length; i++) {
            p[i] = 0x00777777;
            zb[i] = 0;
            lm[i] = ambientcolor;
            lb[i] = 0;
        }
    }

    public void process() {
        processing = true;
        Collections.sort(imageRequests, new Comparator<ImageRequest>() {

            @Override
            public int compare(ImageRequest a0, ImageRequest a1) {
                return a0.zDepth - a1.zDepth;
            }
        });
        for (int i = 0; i < imageRequests.size(); i++) {
            final ImageRequest ir = imageRequests.get(i);
            setzDept(ir.zDepth);
            drawImage(ir.image, ir.offx, ir.offy);
        }
        for (int i = 0; i < lightRequest.size(); i++) {
            LightRequest l = lightRequest.get(i);
            drawLightRequest(l.light, l.locX, l.locY);
        }
        if (lightRequest.size() > 0) {
            for (int i = 0; i < p.length; i++) {
                int lightMapColor = lm[i];
                float r = ((lightMapColor >> 16) & 0xff) / 255f;
                float g = ((lightMapColor >> 8) & 0xff) / 255f;
                float b = (lightMapColor & 0xff) / 255f;
                p[i] = ((int) (((p[i] >> 16) & 0xff) * r) << 16 | (int) (((p[i] >> 8) & 0xff) * g) << 8 | (int) ((p[i] & 0xff) * b));
            }
        }
        imageRequests.clear();
        lightRequest.clear();
        processing = false;
    }

    public void setPixel(int x, int y, int value) {
        int alfa = ((value >> 24) & 0xff);
        if ((x < 0 || x >= pW || y < 0 || y >= pH) || alfa == 0) {
            return;
        }
        int index = x + y * pW;
        if (zb[index] > zDept) {
            return;
        }

        zb[index] = zDept;
        if (alfa == 0xff) {
            p[index] = value;
        } else {
            float alfaDevided = alfa / 256f;
            int pixelColor = p[index];
            final int redpart = ((pixelColor >> 16) & 0xff);
            final int greenPart = ((pixelColor >> 8) & 0xff);
            final int bluePart = pixelColor & 0xff;
            int newRed = redpart - (int) ((redpart - ((value >> 16) & 0xff)) * alfaDevided);
            int newGreen = greenPart - (int) ((greenPart - ((value >> 8) & 0xff)) * alfaDevided);
            int newBlue = bluePart - (int) ((bluePart - (value & 0xff)) * alfaDevided);

            p[index] = (newRed << 16 | newGreen << 8 | newBlue);
        }
    }


    public void setLightBlock(int x, int y, int value) {

        if (x < 0 || x >= pW || y < 0 || y >= pH) {
            return;
        }
        if (zb[x + y * pW] > zDept) {
            return;
        }
        lb[x + y * pW] = value;
    }

    public void setLightMap(int x, int y, int value) {

        if (x < 0 || x >= pW || y < 0 || y >= pH) {
            return;
        }
        int baseColor = lm[x + y * pW];
        int maxRed = Math.max(((baseColor >> 16) & 0xff), (value >> 16) & 0xff);
        int maxGreen = Math.max((baseColor >> 8) & 0xff, (value >> 8) & 0xff);
        int maxBlue = Math.max(baseColor & 0xff, value & 0xff);
        lm[x + y * pW] = (maxRed << 16 | maxGreen << 8 | maxBlue);
    }

    public void drawText(String text, int offx, int offy, int color) {
        int len = text.length();
        int imageWith = font.getFontImage().getW();
        int offset = 0;
        for (int i = 0; i < len; i++) {
            int num = text.codePointAt(i);
            int[] widths = font.getWidths();
            for (int h = 0; h < font.getFontImage().getH(); h++) {
                for (int w = 0; w < widths[num]; w++) {
                    if (font.getFontImage().getP()[(w + font.getOffsets()[num]) + h * imageWith] == 0xffffffff) {
                        setPixel(offx + w + offset, offy + h, color);
                    }
                }
            }
            offset += font.getWidths()[num];
        }
    }

    public void drawImageTile(final ImageTile image, int offx, int offy, int tileX, int tileY) {

        if (image.isAlfa() && !processing) {
            imageRequests.add(new ImageRequest(image.getTileImage(tileX, tileY), zDept, offx, offy));
            return;
        }

        if (offx < -image.getTileW()) {
            return;
        }
        if (offy < -image.getTileH()) {
            return;
        }
        if (offx >= pW) {
            return;
        }
        if (offy >= pH) {
            return;
        }

        int newX = 0;
        int newY = 0;
        int newWith = image.getTileW();
        int newHeight = image.getTileH();

        if (offx < 0) {
            newX -= offx;
        }
        if (offy < 0) {
            newY -= offy;
        }
        if (newWith + offx > pW) {
            newWith -= (newWith + offx - pW);
        }
        if (newHeight + offy > pW) {
            newHeight -= (newHeight + offy - pH);
        }
        for (int y = newY; y < newHeight; y++) {
            for (int x = newX; x < newWith; x++) {
                setPixel(x + offx, y + offy,
                        image.getP()[(x + tileX * image.getTileW()) + (y + tileY * image.getTileH()) * image.getW()]);
                setLightBlock(x + offx, y + offy, image.getLightBlock());
            }
        }
    }

    public void drawImage(final Image image, int offx, int offy) {
        if (image.isAlfa() && !processing) {
            imageRequests.add(new ImageRequest(image, zDept, offx, offy));
            return;
        }
        if (offx < -image.getW()) {
            return;
        }
        if (offy < -image.getH()) {
            return;
        }
        if (offx >= pW) {
            return;
        }
        if (offy >= pH) {
            return;
        }

        int newX = 0;
        int newY = 0;
        int newWith = image.getW();
        int newHeight = image.getH();
        int imageWith = image.getW();

        if (offx < 0) {
            newX -= offx;
        }
        if (offy < 0) {
            newY -= offy;
        }
        if (newWith + offx > pW) {
            newWith -= (newWith + offx - pW);
        }
        if (newHeight + offy > pW) {
            newHeight -= (newHeight + offy - pH);
        }
        for (int y = newY; y < newHeight; y++) {
            for (int x = newX; x < newWith; x++) {
                setPixel(x + offx, y + offy, image.getP()[x + y * imageWith]);
                setLightBlock(x + offx, y + offy, image.getLightBlock());

            }
        }
    }
    public void drawLine(final Vector2D p1, final Vector2D p2, int color) {
        drawBresenhamLine((int)p1.getX(), (int)p1.getY(), (int)p2.getX(), (int)p2.getY(), color);
    }

    public void drawRect(int offx, int offy, int width, int height, int color) {
        for (int y = 0; y <= height; y++) {
            setPixel(offx, y + offy, color);
            setPixel(offx + width, y + offy, color);
        }
        for (int x = 0; x < width; x++) {
            setPixel(x + offx, offy, color);
            setPixel(x + offx, offy + height, color);

        }
    }

    public void drawBresenhamLine(int x0, int y0, int x1, int y1, int color) {
        int dx = Math.abs(x1 - x0);
        int dy = Math.abs(y1 - y0);
        int sx = x0 < x1 ? 1 : -1;
        int sy = y0 < y1 ? 1 : -1;
        int err = dx - dy;
        int e2;
        while (true) {
            int screenX = x0;
            int screenY = y0;
//			setLightMap(screenX,  screenY,  lightColor);
            setPixel(screenX, screenY, color);
            if (x0 == x1 && y0 == y1) {
                break;
            }
            e2 = 2 * err;
            if (e2 > -1 * dy) {
                err -= dy;
                x0 += sx;
            }
            if (e2 < dx) {
                err += dx;
                y0 += sy;
            }
        }

    }

    public void drawFillRec(int offx, int offy, int width, int height, int color) {
//		if (offx < -width) {
//			return;
//		}
//		if (offy < -height) {
//			return;
//		}
//		if (offx >= pW) {
//			return;
//		}
//		if (offy >= pH) {
//			return;
//		}

//		int newX = 0;
//		int newY = 0;
//		int newWith = width;
//		int newHeight = height;
//
//		if (offx < 0) {
//			newX -= offx;
//		}
//		if (offy < 0) {
//			newY -= offy;
//		}
//		if (newWith + offx > pW) {
//			newWith -= (newWith + offx - pW);
//		}
//		if (newHeight + offy > pW) {
//			newHeight -= (newHeight + offy - pH);
//		}
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                setPixel(offx + x, offy + y, color);

            }
        }
    }

    public void drawLight(Light l, int offX, int offY) {
        lightRequest.add(new LightRequest(l, offX, offY));
    }

    private void drawLightRequest(Light l, int offx, int offy) {

        for (int i = 0; i <= l.getDiameter(); i++) {
            drawLightLine(l, l.getRadius(), l.getRadius(), i, 0, offx, offy);
            drawLightLine(l, l.getRadius(), l.getRadius(), i, l.getDiameter(), offx, offy);
            drawLightLine(l, l.getRadius(), l.getRadius(), 0, i, offx, offy);
            drawLightLine(l, l.getRadius(), l.getRadius(), l.getDiameter(), i, offx, offy);
        }
    }

    private void drawLightLine(Light l, int x0, int y0, int x1, int y1, int offx, int offy) {
        int dx = Math.abs(x1 - x0);
        int dy = Math.abs(y1 - y0);
        int sx = x0 < x1 ? 1 : -1;
        int sy = y0 < y1 ? 1 : -1;
        int err = dx - dy;
        int e2;
        while (true) {
            int screenX = x0 - l.getRadius() + offx;
            int screenY = y0 - l.getRadius() + offy;
            if (screenX < 0 || screenX >= pW || screenY < 0 || screenY >= pH) {
                return;
            }
            int lightColor = l.getLightValue(x0, y0);
            if (lightColor == 0) {
                return;
            }
            if (lb[screenX + screenY * pW] == Light.FULL) {
                return;
            }
            setLightMap(screenX, screenY, lightColor);
            if (x0 == x1 && y0 == y1) {
                break;
            }
            e2 = 2 * err;
            if (e2 > -1 * dy) {
                err -= dy;
                x0 += sx;
            }
            if (e2 < dx) {
                err += dx;
                y0 += sy;
            }
        }
    }

    public int getzDept() {
        return zDept;
    }

    public void setzDept(int zDept) {
        this.zDept = zDept;
    }

    public int getAmbientcolor() {
        return ambientcolor;
    }

    public void setAmbientcolor(int ambientcolor) {
        this.ambientcolor = ambientcolor;
    }


}
