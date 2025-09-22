export interface ActionButtonInfo {
  icon: string;
  label: string;
  action: string;
  severity?: 'primary' | 'secondary' | 'success' | 'info' | 'warning' | 'danger';
  onClick: (rowData: any) => void;
  visible?: (rowData: any) => boolean; // retorna true si la acción debe mostrarse
  disabled?: (rowData: any) => boolean; // retorna true si la acción debe estar deshabilitada
}
