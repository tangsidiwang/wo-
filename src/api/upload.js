import { uploadFile } from '@/utils/request'

export const uploadApi = {
  uploadImage: (filePath, onProgress) =>
    uploadFile('/upload/image', filePath, { onProgress }),
  uploadVideo: (filePath, onProgress) =>
    uploadFile('/upload/video', filePath, { name: 'video', onProgress })
}
