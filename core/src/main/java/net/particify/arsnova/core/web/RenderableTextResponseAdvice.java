package net.particify.arsnova.core.web;

import java.io.IOException;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.AbstractMappingJacksonResponseBodyAdvice;

import net.particify.arsnova.core.model.Entity;
import net.particify.arsnova.core.model.EntityRenderingMapping;
import net.particify.arsnova.core.model.ListEntityRenderingMapping;
import net.particify.arsnova.core.model.StringEntityRenderingMapping;
import net.particify.arsnova.core.service.TextRenderingService;

@RestControllerAdvice
public class RenderableTextResponseAdvice extends AbstractMappingJacksonResponseBodyAdvice {
  private static final Logger logger = LoggerFactory.getLogger(RenderableTextResponseAdvice.class);

  private final TextRenderingService textRenderingService;

  public RenderableTextResponseAdvice(final TextRenderingService textRenderingService) {
    this.textRenderingService = textRenderingService;
  }

  @Override
  protected void beforeBodyWriteInternal(
      final MappingJacksonValue mappingJacksonValue,
      final MediaType mediaType,
      final MethodParameter methodParameter,
      final ServerHttpRequest serverHttpRequest,
      final ServerHttpResponse serverHttpResponse) {
    final Object value = mappingJacksonValue.getValue();

    if (value instanceof List) {
      final List<?> list = ((List<?>) value);
      if (list.size() == 0) {
        return;
      }
      if (!(list.get(0) instanceof Entity)) {
        return;
      }
      for (final Entity entity : ((List<? extends Entity>) list)) {
        if (entity != null && !entity.getRenderingMapping().isEmpty()) {
          addRenderedTextToEntity(entity);
        }
      }
      return;
    }

    if (mappingJacksonValue.getValue() instanceof Entity) {
      final Entity entity = (Entity) mappingJacksonValue.getValue();

      if (entity != null && !entity.getRenderingMapping().isEmpty()) {
        addRenderedTextToEntity(entity);
      }
    }
  }

  public void addRenderedTextToEntity(final Entity entity) {
    logger.trace("Renderable fields for entity {}: {}", entity.getId(), entity.getRenderingMapping().size());
    for (final EntityRenderingMapping<?> mapping : entity.getRenderingMapping()) {
      if (mapping instanceof StringEntityRenderingMapping) {
        applyRendering((StringEntityRenderingMapping) mapping);
      } else if (mapping instanceof ListEntityRenderingMapping) {
        applyRendering((ListEntityRenderingMapping) mapping);
      } else {
        throw new IllegalStateException("Unexpected type for mapping.");
      }
    }
  }

  public void applyRendering(final StringEntityRenderingMapping mapping) {
    final String unrenderedText = mapping.getRawValueSupplier().get();
    try {
      final String renderedText = textRenderingService.renderText(unrenderedText, mapping.getOptions());
      mapping.getRenderedValueConsumer().accept(renderedText);
    } catch (final IOException e) {
      logger.error("Failed to render text value.");
    }
  }

  public void applyRendering(final ListEntityRenderingMapping mapping) {
    final List<String> unrenderedTexts = mapping.getRawValueSupplier().get();
    try {
      final List<String> renderedTexts = textRenderingService.renderTexts(unrenderedTexts, mapping.getOptions());
      mapping.getRenderedValueConsumer().accept(renderedTexts);
    } catch (final IOException e) {
      logger.error("Failed to render text value.");
    }
  }
}
