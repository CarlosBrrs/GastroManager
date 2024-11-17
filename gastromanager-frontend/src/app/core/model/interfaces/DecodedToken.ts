export interface DecodedToken {
  uuid: string;
  sub: string;
  roles: string[];
  exp: number;
  iat: number;
}
