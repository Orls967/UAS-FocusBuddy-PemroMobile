---
name: Academic Focus System
colors:
  surface: '#f8f9ff'
  surface-dim: '#cbdbf5'
  surface-bright: '#f8f9ff'
  surface-container-lowest: '#ffffff'
  surface-container-low: '#eff4ff'
  surface-container: '#e5eeff'
  surface-container-high: '#dce9ff'
  surface-container-highest: '#d3e4fe'
  on-surface: '#0b1c30'
  on-surface-variant: '#43474d'
  inverse-surface: '#213145'
  inverse-on-surface: '#eaf1ff'
  outline: '#73777e'
  outline-variant: '#c3c6ce'
  surface-tint: '#436182'
  primary: '#002542'
  on-primary: '#ffffff'
  primary-container: '#1b3b5a'
  on-primary-container: '#87a5ca'
  inverse-primary: '#abc9ef'
  secondary: '#615e57'
  on-secondary: '#ffffff'
  secondary-container: '#e7e2d9'
  on-secondary-container: '#67645d'
  tertiary: '#500006'
  on-tertiary: '#ffffff'
  tertiary-container: '#701617'
  on-tertiary-container: '#fa7d75'
  error: '#ba1a1a'
  on-error: '#ffffff'
  error-container: '#ffdad6'
  on-error-container: '#93000a'
  primary-fixed: '#d1e4ff'
  primary-fixed-dim: '#abc9ef'
  on-primary-fixed: '#001d35'
  on-primary-fixed-variant: '#2a4968'
  secondary-fixed: '#e7e2d9'
  secondary-fixed-dim: '#cbc6bd'
  on-secondary-fixed: '#1d1b16'
  on-secondary-fixed-variant: '#494640'
  tertiary-fixed: '#ffdad7'
  tertiary-fixed-dim: '#ffb3ad'
  on-tertiary-fixed: '#410004'
  on-tertiary-fixed-variant: '#832423'
  background: '#f8f9ff'
  on-background: '#0b1c30'
  surface-variant: '#d3e4fe'
typography:
  headline-lg:
    fontFamily: Manrope
    fontSize: 32px
    fontWeight: '800'
    lineHeight: 40px
    letterSpacing: -0.02em
  headline-md:
    fontFamily: Manrope
    fontSize: 24px
    fontWeight: '700'
    lineHeight: 32px
  headline-sm:
    fontFamily: Manrope
    fontSize: 20px
    fontWeight: '600'
    lineHeight: 28px
  body-lg:
    fontFamily: Hanken Grotesk
    fontSize: 18px
    fontWeight: '400'
    lineHeight: 28px
  body-md:
    fontFamily: Hanken Grotesk
    fontSize: 16px
    fontWeight: '400'
    lineHeight: 24px
  label-lg:
    fontFamily: JetBrains Mono
    fontSize: 14px
    fontWeight: '500'
    lineHeight: 20px
    letterSpacing: 0.05em
  label-md:
    fontFamily: JetBrains Mono
    fontSize: 12px
    fontWeight: '500'
    lineHeight: 16px
  headline-lg-mobile:
    fontFamily: Manrope
    fontSize: 28px
    fontWeight: '800'
    lineHeight: 36px
rounded:
  sm: 0.25rem
  DEFAULT: 0.5rem
  md: 0.75rem
  lg: 1rem
  xl: 1.5rem
  full: 9999px
spacing:
  base: 8px
  gutter: 16px
  margin-mobile: 20px
  margin-desktop: 64px
  container-padding: 24px
  stack-gap: 12px
---

## Brand & Style

The design system is centered on **Cognitive Clarity**. It targets university students who require a distraction-free, high-performance environment that feels both academically rigorous and emotionally supportive.

The visual style is **Corporate / Modern** with a distinct **Material 3 (Jetpack Compose)** influence. It utilizes a "Soft-Productivity" aesthetic: high-functionality layouts paired with approachable rounded corners and a calming color palette. The interface avoids aggressive "hacker-style" productivity tropes in favor of a clean, structured, and friendly academic assistant vibe.

**Key Brand Pillars:**
- **Intentionality:** Every element serves a specific focus-related purpose.
- **Academic Reliability:** A sturdy Navy base provides a sense of institutional trust.
- **Mental Space:** Generous whitespace and cream accents prevent "study-burnout" visual fatigue.
- **Momentum:** Subtle elevations and smooth transitions provide tactile feedback for task completion.

## Colors

The palette is anchored by **Deep Navy (#1B3B5A)**, representing focus and depth of thought. This is contrasted by **Warm Beige/Cream (#F5EFE6)** used for secondary surfaces to soften the overall UI and provide a paper-like feel for reading and task management.

- **Primary:** Navy Blue. Used for branding, primary actions, and active states.
- **Secondary:** Warm Beige. Used for container backgrounds and subtle section differentiation.
- **Tertiary:** Muted Coral/Salmon (#E86F68). Used sparingly for destructive actions (Stop, Delete) or high-priority alerts to provide a visible but non-jarring contrast.
- **Neutral:** A range of Slate Grays. Used for body text, secondary icons, and borders.
- **Semantic:** Success (Emerald), Warning (Amber), and Info (Azure) should be muted to match the professional tone.

## Typography

This design system uses a tri-font strategy to balance character and utility:
1. **Manrope (Headlines):** A modern geometric sans-serif that provides a clean, professional "tech-forward" look for page titles and section headers.
2. **Hanken Grotesk (Body):** Highly legible and approachable, used for all long-form text and task descriptions to reduce cognitive load.
3. **JetBrains Mono (Labels/Data):** A monospaced font used for timers, progress percentages, and priority tags. This evokes a sense of "data" and "precision" suitable for a study tool.

**Scaling:** On mobile devices, headline sizes should reduce by 15-20% to ensure titles do not wrap awkwardly. Line heights are kept generous (1.5x for body) to maintain a feeling of "breathability."

## Layout & Spacing

The layout follows a **Fluid Grid** model with strict adherence to an 8px spacing rhythm. 

- **Mobile:** A single-column vertical stack with 20px side margins. Elements utilize "full-bleed" style cards to maximize horizontal space for task names.
- **Desktop/Tablet:** A 12-column grid where content is typically centered in a max-width container (1200px). Sidebars for navigation and secondary "Stats" widgets should occupy 3 columns.
- **Rhythm:** Use `stack-gap` (12px) for related items within a card and `gutter` (16px) for the gap between distinct UI components.

## Elevation & Depth

Hierarchy is established through **Tonal Layers** and **Ambient Shadows** rather than heavy borders.

1. **The Base:** The primary background is the lightest neutral (#F8FAFC).
2. **The Canvas:** Cards and containers use the pure White (#FFFFFF) surface with a very soft, diffused shadow (`y: 4px, blur: 12px, opacity: 0.05, color: #1B3B5A`).
3. **Interactivity:** On hover or press, cards should lift slightly (increasing shadow spread) or gain a subtle Navy stroke (1px, 10% opacity).
4. **The "Focus" Layer:** When a timer is active, the background should dim or apply a 10px backdrop blur to non-essential elements, pushing the timer card to the visual forefront.

## Shapes

In alignment with Jetpack Compose standards, the system uses a progressive rounding strategy:
- **Small Components (Chips, Inputs):** 8px (`rounded-md`).
- **Standard Components (Buttons, List Items):** 12px (`rounded-lg`).
- **Large Containers (Cards, Modals, Bottom Sheets):** 24px (`rounded-2xl`).

Large rounded corners (24px) are a signature of this design system, creating a friendly, "friendly-app" feel that contrasts with the serious Navy blue color.

## Components

### Buttons
- **Primary:** Solid Navy (#1B3B5A) with White text. 12px rounded corners. Height: 48px.
- **Secondary/Outlined:** Navy 1.5px stroke, no fill, Navy text.
- **Tertiary (Stop/Log Out):** Solid Coral (#E86F68) with White text.

### Cards
- White background, 24px rounded corners, soft ambient shadow.
- Inner padding: 24px.
- Use a 4px Navy left-accent border for "Active" tasks.

### Inputs
- Background: Warm Beige (#F5EFE6) at 40% opacity.
- Border: 1.5px Slate Gray, turning Navy on focus.
- 12px rounded corners.

### Chips & Tags
- Used for priority (High/Medium/Low). 
- **High:** Tertiary (Coral) tint background with dark text. 
- **Style:** Capsule-shaped (fully rounded), JetBrains Mono font.

### Progress Indicators
- Circular timers should have a 12px stroke width.
- Background track: Warm Beige.
- Active track: Primary Navy.

### List Items
- Individually wrapped in cards rather than a flat list. 
- Include a Checkbox on the left and a "Drag handle" or "Priority tag" on the right.