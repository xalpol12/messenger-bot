export interface Image {
  body: File,
}

export interface ImageInfo {
  id: string,
  name: string,
  url: string,
  type: string,
  size: number,
  createdAt: string,
}
