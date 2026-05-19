import type { NextConfig } from "next";

const nextConfig: NextConfig = {
  output: "standalone",
  async rewrites() {
    const backendApiUrl = (process.env.BACKEND_API_URL || "http://localhost:8080").replace(/\/+$/, "");

    return [
      {
        source: "/backend-api/:path*",
        destination: `${backendApiUrl}/api/:path*`,
      },
    ];
  },
};

export default nextConfig;
