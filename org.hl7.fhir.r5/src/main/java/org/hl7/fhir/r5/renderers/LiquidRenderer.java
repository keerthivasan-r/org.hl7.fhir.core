package org.hl7.fhir.r5.renderers;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.hl7.fhir.exceptions.DefinitionException;
import org.hl7.fhir.exceptions.FHIRException;
import org.hl7.fhir.exceptions.FHIRFormatError;
import org.hl7.fhir.r5.model.DomainResource;
import org.hl7.fhir.r5.model.Resource;
import org.hl7.fhir.r5.renderers.utils.BaseWrappers.ResourceWrapper;
import org.hl7.fhir.r5.renderers.utils.RenderingContext;
import org.hl7.fhir.r5.renderers.utils.Resolver.ResourceContext;
import org.hl7.fhir.r5.utils.EOperationOutcome;
import org.hl7.fhir.r5.utils.LiquidEngine;
import org.hl7.fhir.r5.utils.LiquidEngine.LiquidDocument;
import org.hl7.fhir.utilities.xhtml.NodeType;
import org.hl7.fhir.utilities.xhtml.XhtmlNode;
import org.hl7.fhir.utilities.xhtml.XhtmlParser;

public class LiquidRenderer extends ResourceRenderer {

  private String liquidTemplate;

  public LiquidRenderer(RenderingContext context, String liquidTemplate) {
    super(context);
    this.liquidTemplate = liquidTemplate;
  }

  public LiquidRenderer(RenderingContext context, ResourceContext rcontext, String liquidTemplate) {
    super(context);
    this.rcontext = rcontext;
    this.liquidTemplate = liquidTemplate;
  }
  
  @Override
  public boolean render(XhtmlNode x, DomainResource r) throws FHIRFormatError, DefinitionException, IOException, FHIRException, EOperationOutcome {
    LiquidEngine engine = new LiquidEngine(context.getWorker(), context.getServices());
    XhtmlNode xn;
    try {
      LiquidDocument doc = engine.parse(liquidTemplate, "template");
      String html = engine.evaluate(doc, r, rcontext);
      xn = new XhtmlParser().parseFragment(html);
      if (!x.getName().equals("div"))
        throw new FHIRException("Error in template: Root element is not 'div'");
    } catch (FHIRException | IOException e) {
      xn = new XhtmlNode(NodeType.Element, "div");
      xn.para().b().style("color: maroon").tx("Exception generating Narrative: "+e.getMessage());
    }
    x.getChildNodes().addAll(xn.getChildNodes());
    return true;
  }

  @Override
  public String display(Resource r) throws UnsupportedEncodingException, IOException {
    return "not done yet";
  }

  @Override
  public boolean render(XhtmlNode x, ResourceWrapper r) throws FHIRFormatError, DefinitionException, IOException, FHIRException, EOperationOutcome {
    LiquidEngine engine = new LiquidEngine(context.getWorker(), context.getServices());
    XhtmlNode xn;
    try {
      LiquidDocument doc = engine.parse(liquidTemplate, "template");
      String html = engine.evaluate(doc, r.getBase(), rcontext);
      xn = new XhtmlParser().parseFragment(html);
      if (!x.getName().equals("div"))
        throw new FHIRException("Error in template: Root element is not 'div'");
    } catch (FHIRException | IOException e) {
      xn = new XhtmlNode(NodeType.Element, "div");
      xn.para().b().style("color: maroon").tx("Exception generating Narrative: "+e.getMessage());
    }
    x.getChildNodes().addAll(xn.getChildNodes());
    return true;
  }

}
