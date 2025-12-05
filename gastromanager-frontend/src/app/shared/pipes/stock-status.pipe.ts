import {Pipe, PipeTransform} from '@angular/core';

@Pipe({
  name: 'stockStatus',
  standalone: true
})
export class StockStatusPipe implements PipeTransform {
  transform(stock: number, minStock?: number, unit?: string): string {
    if (!stock && stock !== 0) return 'N/A';

    const formattedStock = stock.toLocaleString('es-CO');
    const unitSuffix = unit ? ` ${unit}` : '';

    // Si se proporciona stock mínimo, comparar
    if (minStock !== undefined && stock < minStock) {
      return `⚠️ ${formattedStock}${unitSuffix} (Bajo stock)`;
    }

    // Stock normal o bueno
    if (stock === 0) {
      return `❌ 0${unitSuffix} (Sin stock)`;
    }

    return `✅ ${formattedStock}${unitSuffix}`;
  }
}

