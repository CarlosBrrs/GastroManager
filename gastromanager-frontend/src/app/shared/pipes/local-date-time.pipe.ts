import {Pipe, PipeTransform} from '@angular/core';

@Pipe({
  name: 'localDateTime',
  standalone: true
})
export class LocalDateTimePipe implements PipeTransform {
  transform(utcDateString: string, format: 'short' | 'medium' | 'long' = 'medium'): string {
    if (!utcDateString) return '';

    try {
      const date = new Date(utcDateString);

      // Verificar que la fecha es válida
      if (Number.isNaN(date.getTime())) {
        return utcDateString;
      }

      const options: Intl.DateTimeFormatOptions = this.getFormatOptions(format);

      // Formatear usando la zona horaria local del navegador
      return new Intl.DateTimeFormat('es-CO', options).format(date);
    } catch (error) {
      console.warn('Error formatting date:', error);
      return utcDateString;
    }
  }

  private getFormatOptions(format: 'short' | 'medium' | 'long'): Intl.DateTimeFormatOptions {
    switch (format) {
      case 'short':
        return {
          year: 'numeric',
          month: '2-digit',
          day: '2-digit',
          hour: '2-digit',
          minute: '2-digit'
        };
      case 'long':
        return {
          year: 'numeric',
          month: 'long',
          day: 'numeric',
          hour: '2-digit',
          minute: '2-digit',
          second: '2-digit',
          hour12: true
        };
      case 'medium':
      default:
        return {
          year: 'numeric',
          month: 'short',
          day: 'numeric',
          hour: '2-digit',
          minute: '2-digit',
          hour12: true
        };
    }
  }
}

