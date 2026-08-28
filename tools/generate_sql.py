#!/usr/bin/env python3
"""Genera database/sample_data.sql a partir de las semillas Kotlin reales."""
import re, os, sys

ROOT = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))
SEED = os.path.join(ROOT, "app/src/main/java/com/educalab/exploravida/data/local/seed")

SYSTEMS = {
    "Systems.DIGESTIVO": "digestivo", "Systems.RESPIRATORIO": "respiratorio",
    "Systems.CIRCULATORIO": "circulatorio", "Systems.MOVIMIENTO": "movimiento",
    "Systems.LIMPIEZA": "limpieza", "Systems.RELACION": "relacion",
}

def read(name):
    with open(os.path.join(SEED, name), encoding="utf-8") as f:
        return f.read()

def strip_comments(src):
    src = re.sub(r"/\*.*?\*/", "", src, flags=re.S)
    out = []
    for line in src.split("\n"):
        # elimina // fuera de comillas
        res, in_str, i = "", False, 0
        while i < len(line):
            c = line[i]
            if c == '"' and (i == 0 or line[i-1] != "\\"):
                in_str = not in_str
            if not in_str and c == "/" and i + 1 < len(line) and line[i+1] == "/":
                break
            res += c
            i += 1
        out.append(res)
    return "\n".join(out)

def find_calls(src, fname):
    """Devuelve la lista de textos de argumentos de cada llamada fname(...)."""
    calls = []
    for m in re.finditer(r"(?<![A-Za-z0-9_.])" + re.escape(fname) + r"\s*\(", src):
        i = m.end()
        depth, in_str, start = 1, False, i
        while i < len(src) and depth > 0:
            c = src[i]
            if c == '"' and src[i-1] != "\\":
                in_str = not in_str
            elif not in_str:
                if c == "(":
                    depth += 1
                elif c == ")":
                    depth -= 1
            i += 1
        calls.append(src[start:i-1])
    return calls

def split_args(text):
    args, depth, in_str, cur = [], 0, False, ""
    i = 0
    while i < len(text):
        c = text[i]
        if c == '"' and (i == 0 or text[i-1] != "\\"):
            in_str = not in_str
            cur += c
        elif not in_str and c in "([":
            depth += 1; cur += c
        elif not in_str and c in ")]":
            depth -= 1; cur += c
        elif not in_str and c == "," and depth == 0:
            args.append(cur.strip()); cur = ""
        else:
            cur += c
        i += 1
    if cur.strip():
        args.append(cur.strip())
    return args

def value(raw):
    raw = raw.strip()
    if "=" in raw and not raw.lstrip().startswith('"'):
        head, _, tail = raw.partition("=")
        if re.fullmatch(r"[A-Za-z_][A-Za-z0-9_]*", head.strip()):
            raw = tail.strip()
    raw = raw.replace("\n", " ").strip()
    if raw in SYSTEMS:
        return SYSTEMS[raw]
    if raw == "null":
        return None
    if raw.startswith('"'):
        parts = re.findall(r'"((?:[^"\\]|\\.)*)"', raw)
        return "".join(parts).replace('\\"', '"').replace("\\n", " ")
    m = re.match(r"^(?:[A-Za-z]+Key|ActivityKind|ExperienceKind|SceneBackground|AnimationKey|IllustrationKey)\.([A-Z_]+)(?:\.name)?$", raw)
    if m:
        return m.group(1)
    m = re.match(r"^RewardEngine\.Rules\.([A-Z_]+)$", raw)
    if m:
        return m.group(1)
    m = re.match(r"^([A-Za-z]+)\.([A-Z_]+)(?:\.name)?$", raw)
    if m:
        return m.group(2)
    if re.fullmatch(r"-?\d+(\.\d+)?f?", raw):
        return float(raw.rstrip("f")) if ("." in raw or "f" in raw) else int(raw)
    return raw

def q(v):
    if v is None:
        return "NULL"
    if isinstance(v, bool):
        return "1" if v else "0"
    if isinstance(v, (int, float)):
        return repr(v)
    return "'" + str(v).replace("'", "''") + "'"

content = strip_comments(read("SeedContent.kt"))
acts = strip_comments(read("SeedActivities.kt"))

DECL = re.compile(r"(?:^|\s)(?:val|var)\s|:\s*(?:String|Int|Float|Long|Boolean|IllustrationKey|AnimationKey)\b")

def is_declaration(row):
    return any(isinstance(v, str) and DECL.search(v) for v in row)

def rows(src, fname, count_expected=None):
    result = [[value(a) for a in split_args(c)] for c in find_calls(src, fname)]
    return [r for r in result if not is_declaration(r)]

systems = rows(content, "LivingSystemEntity")
connections = rows(content, "SystemConnectionEntity")
experiences = rows(content, "LearningExperienceEntity")
steps = [r for r in rows(content, "step") if len(r) >= 6]
elements = [r for r in rows(content, "element") if len(r) >= 7]
activities = rows(acts, "ActivityEntity")
sequences = rows(acts, "SequenceEntity")
items = [r for r in rows(acts, "item") if len(r) >= 5]
challenges = rows(acts, "ConnectionChallengeEntity")
badges = rows(acts, "BadgeEntity")
concepts = rows(acts, "Concept")

def insert(table, columns, values_list):
    out = ["-- " + table + " (" + str(len(values_list)) + " filas)"]
    for row in values_list:
        out.append("INSERT INTO " + table + " (" + ", ".join(columns) + ") VALUES (" +
                   ", ".join(q(v) for v in row) + ");")
    out.append("")
    return "\n".join(out)

lines = []
lines.append("""-- ============================================================
-- ExploraVida 1.0.0 - datos semilla
-- Generado desde SeedContent.kt y SeedActivities.kt (tools/generate_sql.py)
-- La aplicacion siembra estos mismos datos en Room la primera vez que se abre.
-- ============================================================

PRAGMA foreign_keys = ON;
""")

lines.append(insert("living_system",
    ["id", "name", "shortDescription", "colorHex", "iconKey", "orderIndex"], systems))
lines.append(insert("system_connection",
    ["fromSystemId", "toSystemId", "explanation"], connections))
lines.append(insert("learning_experience",
    ["id", "title", "subtitle", "noraIntro", "kind", "orderIndex", "requiredXp", "backgroundKey", "iconKey"],
    experiences))

step_rows = []
for r in steps:
    exp, order, title, text, system, anim, illu = r[0], r[1], r[2], r[3], r[4], r[5], r[6]
    step_rows.append([exp, order, title, text, system, anim, illu])
lines.append(insert("experience_step",
    ["experienceId", "orderIndex", "title", "text", "systemId", "animationKey", "illustrationKey"],
    step_rows))

element_rows = []
for r in elements:
    eid, name, system, desc, x, y, illu = r[0], r[1], r[2], r[3], r[4], r[5], r[6]
    radius = r[7] if len(r) > 7 else 0.062
    element_rows.append([eid, name, system, desc, x, y, radius, illu])
lines.append(insert("interactive_element",
    ["id", "name", "systemId", "description", "x", "y", "radius", "illustrationKey"],
    element_rows))

lines.append(insert("activity",
    ["id", "experienceId", "kind", "title", "prompt", "situation", "difficulty", "xpReward"],
    activities))
lines.append(insert("sequence",
    ["id", "activityId", "title", "explanation"], sequences))

item_rows = []
for r in items:
    seq, label, pos, system, illu = r[0], r[1], r[2], r[3], r[4]
    item_rows.append([seq, label, pos, system, illu])
lines.append(insert("sequence_item",
    ["sequenceId", "label", "correctPosition", "systemId", "illustrationKey"], item_rows))

lines.append(insert("connection_challenge",
    ["activityId", "fromSystemId", "toSystemId", "explanation"], challenges))
lines.append(insert("badge",
    ["id", "name", "description", "iconKey", "ruleKey", "threshold"], badges))

lines.append("""-- Perfil de ejemplo (la app lo crea al terminar el onboarding).
INSERT INTO user_profile (id, alias, avatarId, soundEnabled, hapticsEnabled, onboardingDone, createdAt)
VALUES (1, 'Explorador', 0, 1, 1, 1, 0);

INSERT INTO progress (profileId, xp, level, experiencesCompleted, activitiesCompleted, perfectActivities,
                      journeysCompleted, sequencesSolved, connectionsMade, elementsExplored,
                      discoveries, visitedSystems, updatedAt)
VALUES (1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, '', 0);
""")

page_rows = []
for index, c in enumerate(concepts):
    page_rows.append(["page_" + str(c[0]), 1, c[1], index, c[3], 0])
lines.append(insert("explorer_notebook",
    ["id", "profileId", "title", "pageIndex", "stickerKey", "unlocked"], page_rows))

out = "\n".join(lines)
with open(os.path.join(ROOT, "database/sample_data.sql"), "w", encoding="utf-8") as f:
    f.write(out)

print("systems", len(systems), "connections", len(connections), "experiences", len(experiences))
print("steps", len(step_rows), "elements", len(element_rows), "activities", len(activities))
print("sequences", len(sequences), "items", len(item_rows), "challenges", len(challenges))
print("badges", len(badges), "concepts", len(page_rows))
