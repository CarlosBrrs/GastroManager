import {ChangeDetectionStrategy, Component, computed, inject, signal} from '@angular/core';
import {CommonModule} from '@angular/common';
import {TableComponent} from "../../../../../shared/components/table/table.component";
import {ButtonModule} from "primeng/button";
import {ActionButtonInfo} from "../../../../../core/model/interfaces/action-button-info.interface";
import {Recipe} from "../../../domain/models/recipe.interface";
import {Router} from "@angular/router";

@Component({
  selector: 'gm-management-recipes-page',
  standalone: true,
  imports: [
    CommonModule,
    TableComponent,
    ButtonModule
  ],
  templateUrl: './management-recipes-page.component.html',
  styleUrl: './management-recipes-page.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class ManagementRecipesPageComponent {
  private readonly router = inject(Router);

  // Data dummy para las recetas
  recipes = signal<Recipe[]>([
    {
      uuid: '1',
      name: 'Arroz blanco',
      description: 'Receta básica de arroz blanco',
      cost: 425000.00,
      ingredients: [
        {
          ingredientUuid: '70c392d6-c1fc-492a-a36f-b27f04c4c94f',
          ingredientName: 'Azúcar',
          quantity: 50.0
        },
        {
          ingredientUuid: 'ede5cb73-1d57-452f-9328-d02e6516a6bb',
          ingredientName: 'Harina de trigo',
          quantity: 100.0
        }
      ],
      baseRecipe: null,
      isEnabled: true,
      createdBy: 'manager',
      updatedBy: 'manager',
      createdDate: '2025-10-06T07:50:17.919644Z',
      updatedDate: '2025-10-06T07:50:17.919644Z'
    },
    {
      uuid: '2',
      name: 'Pasta Carbonara',
      description: 'Pasta italiana con salsa de huevo, queso y panceta',
      cost: 12500.00,
      ingredients: [
        {
          ingredientUuid: 'a1b2c3d4',
          ingredientName: 'Pasta',
          quantity: 200.0
        },
        {
          ingredientUuid: 'e5f6g7h8',
          ingredientName: 'Huevos',
          quantity: 3.0
        },
        {
          ingredientUuid: 'i9j0k1l2',
          ingredientName: 'Queso parmesano',
          quantity: 50.0
        },
        {
          ingredientUuid: 'm3n4o5p6',
          ingredientName: 'Panceta',
          quantity: 100.0
        }
      ],
      baseRecipe: null,
      isEnabled: true,
      createdBy: 'chef',
      updatedBy: 'chef',
      createdDate: '2025-10-05T10:30:00.000000Z',
      updatedDate: '2025-10-05T10:30:00.000000Z'
    },
    {
      uuid: '3',
      name: 'Pizza Margherita',
      description: 'Pizza con salsa de tomate, mozzarella y albahaca',
      cost: 15000.00,
      ingredients: [
        {
          ingredientUuid: 'q7r8s9t0',
          ingredientName: 'Masa de pizza',
          quantity: 250.0
        },
        {
          ingredientUuid: 'u1v2w3x4',
          ingredientName: 'Salsa de tomate',
          quantity: 100.0
        },
        {
          ingredientUuid: 'y5z6a7b8',
          ingredientName: 'Queso mozzarella',
          quantity: 150.0
        },
        {
          ingredientUuid: 'c9d0e1f2',
          ingredientName: 'Albahaca fresca',
          quantity: 10.0
        }
      ],
      baseRecipe: null,
      isEnabled: true,
      createdBy: 'chef',
      updatedBy: 'manager',
      createdDate: '2025-10-04T14:20:00.000000Z',
      updatedDate: '2025-10-06T08:15:00.000000Z'
    },
    {
      uuid: '4',
      name: 'Hamburguesa Clásica',
      description: 'Hamburguesa de carne con queso, lechuga y tomate',
      cost: 8000.00,
      ingredients: [
        {
          ingredientUuid: 'g3h4i5j6',
          ingredientName: 'Carne molida',
          quantity: 150.0
        },
        {
          ingredientUuid: 'k7l8m9n0',
          ingredientName: 'Pan de hamburguesa',
          quantity: 1.0
        },
        {
          ingredientUuid: 'o1p2q3r4',
          ingredientName: 'Queso cheddar',
          quantity: 30.0
        },
        {
          ingredientUuid: 's5t6u7v8',
          ingredientName: 'Lechuga',
          quantity: 20.0
        },
        {
          ingredientUuid: 'w9x0y1z2',
          ingredientName: 'Tomate',
          quantity: 30.0
        }
      ],
      baseRecipe: null,
      isEnabled: true,
      createdBy: 'chef',
      updatedBy: 'chef',
      createdDate: '2025-10-03T09:00:00.000000Z',
      updatedDate: '2025-10-03T09:00:00.000000Z'
    },
    {
      uuid: '5',
      name: 'Ensalada César Premium',
      description: 'Ensalada césar con ingredientes premium basada en la receta clásica',
      cost: 12500.00,
      ingredients: [
        {
          ingredientUuid: 'a3b4c5d6',
          ingredientName: 'Lechuga romana',
          quantity: 100.0
        },
        {
          ingredientUuid: 'e7f8g9h0',
          ingredientName: 'Pollo premium',
          quantity: 150.0
        },
        {
          ingredientUuid: 'i1j2k3l4',
          ingredientName: 'Queso parmesano',
          quantity: 40.0
        },
        {
          ingredientUuid: 'm5n6o7p8',
          ingredientName: 'Aderezo césar',
          quantity: 60.0
        },
        {
          ingredientUuid: 'x1y2z3a4',
          ingredientName: 'Croutones artesanales',
          quantity: 30.0
        }
      ],
      baseRecipe: {
        uuid: '6',
        name: 'Ensalada César Clásica',
        description: 'Receta base de ensalada césar',
        cost: 9500.00,
        ingredients: [
          {
            ingredientUuid: 'a3b4c5d6',
            ingredientName: 'Lechuga romana',
            quantity: 100.0
          },
          {
            ingredientUuid: 'e7f8g9h0',
            ingredientName: 'Pollo',
            quantity: 120.0
          },
          {
            ingredientUuid: 'i1j2k3l4',
            ingredientName: 'Queso parmesano',
            quantity: 30.0
          },
          {
            ingredientUuid: 'm5n6o7p8',
            ingredientName: 'Aderezo césar',
            quantity: 50.0
          }
        ],
        baseRecipe: null,
        isEnabled: true,
        createdBy: 'chef',
        updatedBy: 'chef',
        createdDate: '2025-10-01T08:00:00.000000Z',
        updatedDate: '2025-10-01T08:00:00.000000Z'
      },
      isEnabled: true,
      createdBy: 'chef',
      updatedBy: 'manager',
      createdDate: '2025-10-02T11:45:00.000000Z',
      updatedDate: '2025-10-06T07:00:00.000000Z'
    }
  ]);

  loading = signal(false);
  totalRecords = signal(5);
  selectedRecipe = signal<Recipe | null>(null);

  // Configuración de columnas para la tabla
  tableColumns = [
    {
      field: 'name',
      header: 'Nombre',
      sortable: true
    },
    {
      field: 'cost',
      header: 'Costo de Producción',
      sortable: true,
      pipe: 'currency'
    },
    {
      field: 'ingredients',
      header: 'Ingredientes',
      sortable: false,
      transform: (ingredients: any[]) => this.formatIngredients(ingredients)
    },
    {
      field: 'baseRecipe',
      header: 'Receta Base',
      sortable: false,
      clickable: true,
      transform: (baseRecipe: any) => this.formatBaseRecipe(baseRecipe)
    }
  ];

  // Acciones para cada fila
  actions: ActionButtonInfo[] = [
    {
      action: 'view',
      icon: 'pi pi-eye',
      label: 'Ver',
      severity: 'info',
      onClick: (rowData) => this.viewRecipe(rowData)
    },
    {
      action: 'edit',
      icon: 'pi pi-pencil',
      label: 'Editar',
      severity: 'warning',
      onClick: (rowData) => this.editRecipe(rowData)
    },
    {
      action: 'delete',
      icon: 'pi pi-trash',
      label: 'Eliminar',
      severity: 'danger',
      onClick: (rowData) => this.deleteRecipe(rowData)
    }
  ];

  onAddRecipe() {
    console.log('➕ Navegando a crear receta...');
    this.router.navigate(['/management/recipes/create']);
  }

  handleRecipeSelected(recipe: Recipe) {
    console.log('📋 Receta seleccionada:', recipe);
    this.selectedRecipe.set(recipe);
  }

  changePageHandler(event: { page: number; size: number }) {
    console.log('📄 Cambiando de página:', event);
    // TODO: Implementar paginación real
  }

  private viewRecipe(recipe: Recipe) {
    console.log('👁️ Ver receta:', recipe);
    // TODO: Implementar vista de detalle de receta
  }

  private editRecipe(recipe: Recipe) {
    console.log('✏️ Editar receta:', recipe);
    // TODO: Implementar edición de receta
  }

  private deleteRecipe(recipe: Recipe) {
    console.log('🗑️ Eliminar receta:', recipe);
    // TODO: Implementar eliminación de receta
  }

  private formatIngredients(ingredients: any[]): string {
    if (!ingredients || ingredients.length === 0) {
      return 'Sin ingredientes';
    }

    // Formato compacto: "Azúcar (50g), Harina (100g), ..."
    const formatted = ingredients
      .map(ing => `${ing.ingredientName} (${ing.quantity}g)`)
      .join(', ');

    // Si es muy largo, mostrar solo los primeros 3 y agregar "..."
    if (ingredients.length > 3) {
      const first3 = ingredients.slice(0, 3)
        .map(ing => `${ing.ingredientName} (${ing.quantity}g)`)
        .join(', ');
      return `${first3} +${ingredients.length - 3} más`;
    }

    return formatted;
  }

  private formatBaseRecipe(baseRecipe: any): string {
    if (!baseRecipe) {
      return '-';
    }
    return baseRecipe.name;
  }
}
