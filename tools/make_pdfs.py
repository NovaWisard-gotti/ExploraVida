#!/usr/bin/env python3
"""Convierte los documentos Markdown de docs/ en PDF reales con ReportLab."""
import os, re, sys
from reportlab.lib.pagesizes import A4
from reportlab.lib.styles import getSampleStyleSheet, ParagraphStyle
from reportlab.lib.units import mm
from reportlab.lib import colors
from reportlab.platypus import (SimpleDocTemplate, Paragraph, Spacer, Table,
                                TableStyle, PageBreak)

ROOT = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))
DOCS = os.path.join(ROOT, "docs")
OUT = os.path.join(DOCS, "pdf")
os.makedirs(OUT, exist_ok=True)

DEEP = colors.HexColor("#0E2A47")
LIME = colors.HexColor("#2E7D52")
AMBER = colors.HexColor("#B36B00")
GREY = colors.HexColor("#EDF2F7")

styles = getSampleStyleSheet()
S = {
    "h1": ParagraphStyle("h1", parent=styles["Heading1"], fontName="Helvetica-Bold",
                         fontSize=19, leading=23, textColor=DEEP, spaceBefore=6, spaceAfter=10),
    "h2": ParagraphStyle("h2", parent=styles["Heading2"], fontName="Helvetica-Bold",
                         fontSize=14, leading=18, textColor=LIME, spaceBefore=12, spaceAfter=6),
    "h3": ParagraphStyle("h3", parent=styles["Heading3"], fontName="Helvetica-Bold",
                         fontSize=11.5, leading=15, textColor=AMBER, spaceBefore=9, spaceAfter=4),
    "p": ParagraphStyle("p", parent=styles["BodyText"], fontName="Helvetica",
                        fontSize=9.6, leading=14, spaceAfter=5),
    "li": ParagraphStyle("li", parent=styles["BodyText"], fontName="Helvetica",
                         fontSize=9.6, leading=14, leftIndent=12, bulletIndent=3, spaceAfter=3),
    "code": ParagraphStyle("code", parent=styles["BodyText"], fontName="Courier",
                           fontSize=8, leading=10.5, textColor=DEEP,
                           backColor=GREY, borderPadding=5, spaceAfter=7),
    "cell": ParagraphStyle("cell", parent=styles["BodyText"], fontName="Helvetica",
                           fontSize=8.2, leading=11),
    "cellh": ParagraphStyle("cellh", parent=styles["BodyText"], fontName="Helvetica-Bold",
                            fontSize=8.2, leading=11, textColor=colors.white),
    "quote": ParagraphStyle("quote", parent=styles["BodyText"], fontName="Helvetica-Oblique",
                            fontSize=9.4, leading=13.5, leftIndent=14, textColor=DEEP, spaceAfter=6),
}

def inline(text):
    text = text.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;")
    text = re.sub(r"`([^`]+)`", r'<font face="Courier" size="8.6">\1</font>', text)
    text = re.sub(r"\*\*([^*]+)\*\*", r"<b>\1</b>", text)
    text = re.sub(r"(?<!\*)\*([^*]+)\*(?!\*)", r"<i>\1</i>", text)
    return text

def split_row(line):
    return [c.strip() for c in line.strip().strip("|").split("|")]

def build_table(rows, width):
    header = [Paragraph(inline(c), S["cellh"]) for c in rows[0]]
    body = [[Paragraph(inline(c), S["cell"]) for c in r] for r in rows[1:]]
    cols = len(rows[0])
    data = [header] + body
    table = Table(data, colWidths=[width / cols] * cols, repeatRows=1)
    table.setStyle(TableStyle([
        ("BACKGROUND", (0, 0), (-1, 0), DEEP),
        ("TEXTCOLOR", (0, 0), (-1, 0), colors.white),
        ("VALIGN", (0, 0), (-1, -1), "TOP"),
        ("GRID", (0, 0), (-1, -1), 0.4, colors.HexColor("#B9C6D4")),
        ("ROWBACKGROUNDS", (0, 1), (-1, -1), [colors.white, GREY]),
        ("LEFTPADDING", (0, 0), (-1, -1), 5),
        ("RIGHTPADDING", (0, 0), (-1, -1), 5),
        ("TOPPADDING", (0, 0), (-1, -1), 3),
        ("BOTTOMPADDING", (0, 0), (-1, -1), 3),
    ]))
    return table

def parse(md, width):
    flow, i = [], 0
    lines = md.split("\n")
    while i < len(lines):
        line = lines[i]
        stripped = line.strip()
        if not stripped:
            i += 1
            continue
        if stripped.startswith("```"):
            block, i = [], i + 1
            while i < len(lines) and not lines[i].strip().startswith("```"):
                block.append(lines[i])
                i += 1
            i += 1
            text = "<br/>".join(
                l.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;")
                 .replace(" ", "&nbsp;") for l in block)
            flow.append(Paragraph(text, S["code"]))
            continue
        if stripped.startswith("|") and i + 1 < len(lines) and set(lines[i+1].strip()) <= set("|-: "):
            rows = [split_row(stripped)]
            i += 2
            while i < len(lines) and lines[i].strip().startswith("|"):
                rows.append(split_row(lines[i]))
                i += 1
            flow.append(Spacer(1, 3))
            flow.append(build_table(rows, width))
            flow.append(Spacer(1, 7))
            continue
        if stripped.startswith("### "):
            flow.append(Paragraph(inline(stripped[4:]), S["h3"]))
        elif stripped.startswith("## "):
            flow.append(Paragraph(inline(stripped[3:]), S["h2"]))
        elif stripped.startswith("# "):
            flow.append(Paragraph(inline(stripped[2:]), S["h1"]))
        elif stripped.startswith("> "):
            flow.append(Paragraph(inline(stripped[2:]), S["quote"]))
        elif re.match(r"^[-*] ", stripped):
            flow.append(Paragraph(inline(stripped[2:]), S["li"], bulletText="\u2022"))
        elif re.match(r"^\d+\. ", stripped):
            num = stripped.split(".", 1)[0]
            flow.append(Paragraph(inline(stripped.split(". ", 1)[1]), S["li"], bulletText=num + "."))
        elif set(stripped) <= set("-—_") and len(stripped) >= 3:
            flow.append(Spacer(1, 6))
        else:
            flow.append(Paragraph(inline(stripped), S["p"]))
        i += 1
    return flow

def footer(canvas, doc):
    canvas.saveState()
    canvas.setFont("Helvetica", 7.5)
    canvas.setFillColor(colors.HexColor("#5A6B7D"))
    canvas.drawString(20 * mm, 12 * mm, "ExploraVida 1.0.0 - El gran laboratorio de la vida")
    canvas.drawRightString(190 * mm, 12 * mm, "Pagina %d" % doc.page)
    canvas.setStrokeColor(colors.HexColor("#C3D0DC"))
    canvas.line(20 * mm, 15 * mm, 190 * mm, 15 * mm)
    canvas.restoreState()

def cover(title, subtitle, width):
    return [
        Spacer(1, 45 * mm),
        Paragraph("EXPLORAVIDA", ParagraphStyle(
            "cover", fontName="Helvetica-Bold", fontSize=30, leading=34,
            textColor=DEEP, alignment=1)),
        Spacer(1, 4 * mm),
        Paragraph("El gran laboratorio de la vida", ParagraphStyle(
            "sub", fontName="Helvetica-Oblique", fontSize=13, textColor=LIME, alignment=1)),
        Spacer(1, 20 * mm),
        Paragraph(title, ParagraphStyle(
            "t", fontName="Helvetica-Bold", fontSize=18, textColor=AMBER, alignment=1)),
        Spacer(1, 5 * mm),
        Paragraph(subtitle, ParagraphStyle(
            "s", fontName="Helvetica", fontSize=10.5, textColor=DEEP, alignment=1, leading=15)),
        PageBreak(),
    ]

JOBS = [
    ("MEMORIA_DESCRIPTIVA", "Memoria descriptiva",
     "Aplicacion Android educativa para ninos de 8 a 12 anos<br/>Version 1.0.0"),
    ("MANUAL_USUARIO", "Manual de usuario",
     "Guia para exploradores de 8 a 12 anos<br/>y para las familias que les acompanan"),
    ("MANUAL_TECNICO", "Manual tecnico",
     "Arquitectura, motores educativos, base de datos y compilacion<br/>Kotlin - Jetpack Compose - Room"),
]

width = A4[0] - 40 * mm
for name, title, subtitle in JOBS:
    src = os.path.join(DOCS, name + ".md")
    dst = os.path.join(OUT, name + ".pdf")
    with open(src, encoding="utf-8") as f:
        md = f.read()
    doc = SimpleDocTemplate(
        dst, pagesize=A4,
        leftMargin=20 * mm, rightMargin=20 * mm,
        topMargin=18 * mm, bottomMargin=20 * mm,
        title="ExploraVida - " + title, author="ExploraVida")
    doc.build(cover(title, subtitle, width) + parse(md, width),
              onFirstPage=footer, onLaterPages=footer)
    print("generado", dst)
