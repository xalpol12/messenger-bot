export interface ImageFormData {
  name?: string,
  customUri?: string,
}

export interface ImageInfo {
  imageId: string,
  name: string,
  url: string,
  type: string,
  size: number,
  createdAt: string,
  lastModifiedAt: string,
}
