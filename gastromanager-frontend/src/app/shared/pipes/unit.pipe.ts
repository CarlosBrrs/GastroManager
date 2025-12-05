import {Pipe, PipeTransform} from '@angular/core';

@Pipe({
  name: 'unit',
  standalone: true
})
export class UnitPipe implements PipeTransform {
  transform(unit: string): string {
    if (!unit) return '';

    // Mapeo de unidades a representaciones más amigables
    const unitMap: Record<string, string> = {
      // Mayúsculas (estándar)
      'KILOGRAM': '📏 kg',
      'GRAM': '📏 g',
      'LITER': '📏 L',
      'MILLILITER': '📏 ml',
      'UNIT': '📏 Unidad(es)',
      'UNITS': '📏 Unidad(es)',
      'POUND': '📏 lb',
      'OUNCE': '📏 oz',
      'GALLON': '📏 gal',
      'CUP': '📏 taza(s)',
      'TABLESPOON': '📏 cda(s)',
      'TEASPOON': '📏 cdta(s)',

      // Minúsculas y variaciones (del backend)
      'g': '📏 g',
      'kg': '📏 kg',
      'mL': '📏 ml',
      'ml': '📏 ml',
      'L': '📏 L',
      'l': '📏 L',
      'units': '📏 Unidad(es)',
      'unit': '📏 Unidad(es)',
      'lb': '📏 lb',
      'oz': '📏 oz'
    };

    // Primero intentar con el valor exacto
    if (unitMap[unit]) {
      return unitMap[unit];
    }

    // Si no existe, intentar con mayúsculas
    const upperUnit = unit.toUpperCase();
    if (unitMap[upperUnit]) {
      return unitMap[upperUnit];
    }

    // Fallback: formatear el string (solo si no está en el mapa)
    const formatted = unit.replace(/_/g, ' ').toLowerCase();
    return `📏 ${formatted.replace(/(^|\s)\S/g, (t) => t.toUpperCase())}`;
  }
}

